---
layout: post
title:  "DTOs with care"
date:   2015-11-14 23:15:00 +0200
categories: dtos 
---
I want to talk about some basic stuff, but it's something that I've seen a great deal in the wild. Badly designed data
objects. Let's take a sufficiently basic example:

We need to model students for some grading application. Simple enough!

You can follow along with the article by checking out the code from [here](https://github.com/OryxGazella/dtos-with-care).
There are additional tests in some of the examples that have been omitted from this post for the sake of brevity.

We start off by capturing the students' first names and last names as fields in an object.

``` bash
git checkout -f 01-plain-old-java-object
```

``` java
public class Student {
    private String firstName;
    private String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
```

Okay, so what's wrong with this? Well, first and foremost, this is a data transfer object or a value object.

1. Should it have mutable state? `Probably not`
2. Should it allow null values? `Probably not`
3. Wait, shouldn't there be equals(), hashCode() and toString() methods defined? `Yes, sir!`

I'll talk about all these problems in turn and offer some solutions.

## A better way

### Get rid of mutable state

Mutable state is a big problem in systems. It makes it difficult to reason about the state of your object, whether it's
been modified in a separate (or same) thread, whether some method has modified it and whether something is null or not. The problems are there and they're real.

So this first step is to get rid of those pesky setters.

Chances are that your IDE is already warning you that these setters are unused, don't ignore these warnings!

"But Franky, `Jackson` needs those setters to deserialize the objects," you object.  Well, that's not true. Try it.
By default, `Jackson` will use field injection, though this is probably not the best way to construct objects.

So here is our first major improvement:

``` bash
git checkout -f 02-remove-setters
```

``` java
public class Student {
    private final String firstName;
    private final String lastName;

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
```

While we're at it. Let's get rid of the get prefix to all of these fields, we aren't a JavaBean, so we don't
(necessarily) need to follow the JavaBean way.

``` bash
git checkout -f 03-un-java-bean
```

``` java
public class Student {
    //...

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }
}

```

### Get rid of and/or limit the use of null

Null is not your friend. You should ban null wherever possible in your system and make nulls overt where you can't do so.

The situation is not helped by the fact that [JSR 303](http://beanvalidation.org/1.0/spec/) defines the `@NotNull`
annotation. This is rather unfortunate. Everything should be `@NotNull` by default, and things that could ever possibly
be null should be marked by a `@Nullable` marker annotation.

Okay, let's think about things a bit and make the concession that not all people have last names -- think Fabio. So we
say that indeed last name could be null, but never the name.

I prefer these over constructors, as they can have an overt name. Joshua Bloch's
[Effective Java](http://www.informit.com/store/effective-java-9780321356680) talks about this in further detail.
It's _item 1,_ in fact.

We also go ahead and create a marker interface to show our intent that the `lastName` field is in fact nullable and mark
the constructor as package private. If you're of the lazy persuasion you can use one of the `@Nullable` declarations
from libraries you use such as `Guava`.

``` bash
git checkout -f 04-thinking-about-null
```

``` java
...
@Nullable
private String lastName;

public static Student create(String firstName, String lastName) {
       if (firstName == null) throw new IllegalArgumentException("Null firstName");
       return new Student(firstName, lastName);
}
...
```

And finally we mark the constructor as private.

Okay, great, but how do we ensure that we fail fast when deserializing junk? As we know, libraries such as Jackson will
use reflection to populate your fields. This one is easy, we just annotate our method with the `@JsonCreator` annotation
and the fields with the `@JsonProperty("fieldName")` property like so:

``` java
...
@JsonCreator
public static Student create(
        @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName) {
        //...
}
...
```

Controlling object deserialization through a method instead of relying on field injection also allows a few fancy things.

In the case where the student object is being created with a user interface, it is quite reasonable to throw an exception
and display an error message when a user tries to create a student with no first name (assuming the ui derives its
validation from the objection creation code above). However, when the DTO is used for the purposes of ferrying information
from one data source to another, it might be better to simply discard invalid objects.

Suppose that we are in the midst of a service that takes a bunch of students from one source, does a lookup on another
system and aggregates the data. Suppose further, that for some or other reason, the model of the Student is not properly
defined upstream. Such that it is possible to have the following returned from the data source:

``` javascript
[
  {
    "firstName": "Fabio"
  },
  {
  },
  {
    "firstName": "Frank",
    "lastName": "Smith"
  },
  {
    "firstName": "Fabio"
  },
  {
    "lastName": "Dracula"
  }
]
```

We'll simulate how this gets deserialized with the following:
``` bash
git checkout -f 05-dealing-with-bad-data
```

``` java
private Collection<Student> simulateBadDataFromApi() {
    return Arrays.asList(
            Student.create("Fabio", null),
            Student.create(null, null),
            Student.create("Frank", "Smith"),
            Student.create("Fabio", null),
            Student.create(null, "Dracula"));
}
```

So now we define a static instance of the student object called `invalidStudent` and instead of throwing an
`IllegalArgumentException` we return this instance for the invalid Students.

``` java
...
public static Student invalidStudent = new Student("Invalid", "Invalid");
...

```

``` java
//...
public static Student create(//...) {
    if (firstName == null) return invalidStudent;
    return new Student(firstName, lastName);
}
```

After we add this, we can write the following test to verify that the `{}` and `{'lastName': 'Dracula'}` objects have
been dropped:

``` java
@Test
public void should_identify_invalid_data() {
    Collection<Student> students = simulateBadDataFromApi().stream()
            .filter(s -> s != Student.invalidStudent)
            .collect(Collectors.toList());

    assertThat(students.size(), is(3));
}
```
The filter uses reference equality in its predicate `s != Student.invalidStudent`, which is fine, since all the invalid
students return the same object instance.

After returning an invalid object instance when the validation rules fail, we also notice that there is a duplicate,
Fabio, that we want to drop.

Okay, no problem, we'll just throw the result into a set and that should remove any duplicates. Well, kind of...

### Equals and Hash Code

The next biggest mistake I've seen in DTOs is improper handling of `equals()` and `hashCode()`. Either it's not implemented.
Only implemented for one or partially implemented. All of these can cause subtle but serious bugs. Modifying the above
test to use a set instead of a list and expecting two elements results in a failure:

``` bash
git checkout -f 06-sets-fail
```

``` java
public class StudentTest {

    @Test
    public void should_identify_invalid_data() {
        Collection<Student> students = simulateBadDataFromApi().stream()
                .filter(s -> s != Student.invalidStudent)
                .collect(Collectors.toSet());

        assertThat(students.size(), is(2));
        assertThat(students, containsInAnyOrder(
                Student.create("Fabio", null),
                Student.create("Frank", "Smith")));
    }

    private Collection<Student> simulateBadDataFromApi() {
        return Arrays.asList(
                Student.create("Fabio", null),
                Student.create(null, null),
                Student.create("Frank", "Smith"),
                Student.create("Fabio", null),
                Student.create(null, "Dracula"));
    }
}
```

The following test fails because hashCode and equals are not implemented. Easy enough, we hit our handy "generate" button
in our IDE and ask it to roll us an equals and hashCode method, taking care to specify which fields are non-null of
course. I'm not going to show this code here, but this makes the test pass easily enough.

``` bash
git checkout -f 07-alt-insert
```

The code has two main problems:

1.  We have no guarantee that when someone else comes on the project and they add fields to the student object that
    they will take care to update the equals and hashCode methods. Even if you have unit test that test for this
    specifically, you're in no way guaranteed that they will be cognisant of these new fields added, especially when
    object creation is done through multiple calls to setter methods.
2.  It's boilerplate code.

Improper equals and hashCode can lead to some subtle but nasty bugs.

There's nothing like a fairly sized DTO which misses a single field in its equals() and hashCode() implementation. This is
particularly pernicious as it is difficult to test equals and hashCode thoroughly, at least manually. I've had unit tests
fail for this exact reason, luckily in the most recent case the fallout was just a test failing, which led me to fixing
the underlying problem...

A colleague of mine recently gave a lightning talk about boilerplate code and how one should take steps to avoid writing
it manually, or even avoid writing it at all.

I fully agree with this view and we've identified a few libraries that can solve this problem. Except for the last of
these which is a language...

#### Lombok

I was introduced to `Lombok` when I joined my first Java project. I was racking my brain as to why my IDE would not
compile the project. I was still wrapping things up on my previous project and was not sitting with the other (30 man!)
development team. After roping in one of the developers I found out that I would need to install the lombok plugin for
eclipse to realise that getters and setters were being synthesized.

The entirety of what we're trying to achieve can be done as follows using `Lombok`

``` bash
git checkout -f 08-lombok
```

``` java
@Data
public class Student {
    private static final Student invalidStudent = 
                    new Student("Invalid", "Invalid");

    private final String firstName;
    @Nullable
    private final String lastName;

    @JsonCreator
    public static Student create(@JsonProperty("firstName") String firstName,
                                 @JsonProperty("lastName") String lastName) {
        if (firstName == null) return invalidStudent;
        Student student = new Student(firstName, lastName);
        return student;
    }
}
```

Or only the `Lombok` relevant part:

``` java
@Data
public class Student {
    private final String firstName;
    private final String lastName;
}
```

Caveats:

The `Lombok` site's written example has a very old version of `Lombok` in the dependency, I was a bit confused when trying
to get the application to run for this example.

Since `Lombok` does an AST transform (modifying the bytecode during compile time), you'll need an IDE plugin or else
your IDE will moan. (Compile still works fine even with the plugin disabled in `IntelliJ`)

`Lombok` does not care about the `@Nullable` interface. Instead, a `@NotNull` annotation should be supplied to the fields
you don't want to be null, :-(.

What you may be after is something that will generate and
update your boilerplate code for you.

#### AutoValue

[AutoValue](https://github.com/google/auto/tree/master/value) was the first of these code generators that I worked with.

The above example would be done as follows:

``` bash
git checkout -f 09-auto-value
```

``` java
@AutoValue
public abstract class Student {

    Student() {
    }

    public static Student invalidStudent = 
            new AutoValue_Student("Invalid", "Invalid");

    @JsonCreator
    public static Student create(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName) {
        if (firstName == null) return invalidStudent;
        return new AutoValue_Student(firstName, lastName);
    }

    public abstract String firstName();

    @Nullable
    public abstract String lastName();
}
```

This is pretty succinct, and to nobody's surprise, the unit test above still passes. Since firstName is not marked with
nullable, the generated constructor in the class AutoValue_Student does a null check on firstName but not on last name.

Similarly, hashCode does not check for null on the firstName but does on the lastName. Here we see another benefit of
immutable objects. We can reason about the state of an object that does not have a setter.

There are two caveats worth mentioning.

1. It's considered best practice to make a package private constructor to discourage subclassing of the abstract class.
2. If you are serializing the object and you've done away with the getX convention you have a to annotate your accessor
methods with @JsonProperty as well. This is a Jackson thing more than anything else, so perhaps it does not really
relate to AutoValue.

#### Immutables 2.0

In terms of features [`Immutables 2.0`](http://immutables.org) gets my vote for best in class. It's very similar to AutoValue and automatically
provides you with a builder!

Just like `AutoValue`, it goes and generates code for you at compile time and gets out of your way otherwise.

`Jackson` serialization and deserialization is a breeze with a simple annotation. No need for a @JsonCreator method in this case.

Assuming we want nothing fancy, the above is perfectly satisfied with the following.

``` java
@Value.Immutable
@JsonSerialize(as = ImmutableStudent.class)
@JsonDeserialize(as = ImmutableStudent.class)
public interface Student {
    String firstName();

    @Nullable
    String lastName();
}
```

And here's the fancy version (we're still using the @JsonCreator here for the purposes of catching invalid stuff):

``` bash
git checkout -f 10-immutables
```

``` java
@Value.Immutable
public interface Student {
    String firstName();

    @Nullable
    String lastName();

    @JsonCreator
    static Student create(
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName) {
        if (firstName == null) return invalidStudent;
        return ImmutableStudent.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

    Student invalidStudent = ImmutableStudent
            .builder()
            .firstName("Invalid")
            .build();
}
```

What you want can also be achieved with an abstract class, but an interface saves some keystrokes. Similar to `AutoValue`,
any interface called `@Nullable` is honoured by the code generator.

#### Other JVM Languages

One of the criticisms leveraged against something such as Lombok is that it is extralinguistic (check the
[presentation](https://docs.google.com/presentation/d/14u_h-lMn7f1rXE1nDiLX0azS3IkgjGl5uxp5jGJ75RE/edit) that's part of
AutoValue's page). So, if you're going to be extralinguistic, why not just use another JVM language.

`Kotlin`, `Scala` and `Groovy` to name a few provide solutions to these problems.

You may have to set your IDE's source roots to pick up the `kotlin`, `groovy` and `scala`
folders to get these to work from within your IDE.

Here is a bare-bones data class in `Groovy`:

``` groovy
import groovy.transform.Immutable

@Immutable
class Student {
    String firstName
    String lastName
}
```

And the same thing in `Kotlin`

``` kotlin
data class Student(val firstName: String, val lastName: String?)

```

This creates the following:

* Getters (without setters)
* A constructor for the arguments

That's it! Both of these can fit in a tweet.

Adding the other fancy stuff for Json deserialization leads to the following full class (import statements omitted) and
the below test for sanitizing input passes just fine.

``` bash
git checkout -f 11-groovy
```

``` groovy
@Immutable
class Student {
    String firstName
    String lastName

    @JsonCreator
    static Student create(@JsonProperty("firstName") String firstName,
                          @JsonProperty("lastName") String lastName) {
        if (firstName == null) return invalidStudent
        new Student(firstName, lastName)
    }

    static Student invalidStudent = new Student("Invalid", "Invalid")
}
```

References to the static instance will have to change from a field to `getInvalidStudent())`.

And the same thing for Kotlin:

``` bash
git checkout -f 12-kotlin
```

<div class="language-kotlin highlighter-rouge"><pre class="highlight"><code><span class="kd">data class</span> <span class="nc">Student</span><span class="p">(</span><span class="kd">val</span> <span class="py">firstName</span><span class="p">:</span> <span class="n">String</span><span class="p">,</span> <span class="kd">val</span> <span class="py">lastName</span><span class="p">:</span> <span class="n">String</span><span class="p">?)</span> <span class="p">{</span>
    <span class="k">companion</span> <span class="kd">object</span> <span>{
        </span><span class="nd">@JsonCreator</span>
        <span class="nd">@JvmStatic</span>
        <span class="k">fun</span> <span class="nf">create</span><span class="p">(</span>
                <span class="n">@JsonProperty</span><span class="p">(</span><span class="s">"firstName"</span><span class="p">)</span> <span class="n">firstName</span><span class="p">:</span> <span class="n">String</span><span class="p">?,</span>
                <span class="n">@JsonProperty</span><span class="p">(</span><span class="s">"lastName"</span><span class="p">)</span> <span class="n">lastName</span><span class="p">:</span> <span class="n">String</span><span class="p">?):</span> <span class="n">Student</span> <span class="p">{</span>
            <span class="k">if</span> <span class="p">(</span><span class="n">firstName</span> <span class="p">==</span> <span class="k">null</span><span class="p">)</span> <span class="k">return</span> <span class="n">invalidStudent</span>
            <span class="k">return</span> <span class="n">Student</span><span class="p">(</span><span class="n">firstName</span><span class="p">,</span> <span class="n">lastName</span><span class="p">)</span>
        <span class="p">}</span>

        <span class="n">@JvmField</span> <span class="kd">val</span> <span class="py">invalidStudent</span> <span class="p">=</span> <span class="n">Student</span><span class="p">(</span><span class="s">"Invalid"</span><span class="p">,</span> <span class="s">"Invalid"</span><span class="p">)</span>
    <span class="p">}</span>
<span class="p">}</span>
</code></pre>
</div>

The following caveats apply. The `@JvmStatic` and `@JvmField` annotations are for Java interop'
and allow us to call the method and field as though they are static.

``` bash
git checkout -f 13-scala
```

And finally in `Scala`:

``` scala
case class Student(firstName: String, lastName: String)

object Student {
  def create(firstName: String, lastName: String): Student = {
    if (firstName == null) return invalidStudent
    Student(firstName, lastName)
  }

  val invalidStudent = new Student("Invalid", "Invalid")
}
```

## Stray observations

### Limit your DTOs to what you're actually processing

One of the biggest mistakes I've seen is people who just take the DTOs as they're defined in the data source
and adding all of the fields from there. This leads to IDE warnings amongst other things. You're also exposing
yourself to unnecessary breaking changes if a field that you're not even using changes in structure.

Listen to your IDE and git rid of any unused fields and methods. This can be problematic in the case of `AutoValue`,
because even unused fields will show up as used as the code generators implement those fields.

### Pay close attention to validation on your data source

A lot of the techniques described here are reactionary measures due to badly defined data models. We try to limit
the defensive coding to a single place -- the deserialization of data, but the best situation is where you don't
need to do any form of defensive coding.

### Don't check in generated code

This should go without saying, but I've seen generated code being checked in, and subsequently modified.

Don't do this. Choose life.


