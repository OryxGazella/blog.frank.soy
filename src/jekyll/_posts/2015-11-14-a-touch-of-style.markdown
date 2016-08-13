---
layout: post
title:  "A touch of style"
date:   2015-11-14 00:03:00 +0200
categories: css 
---

Well, after letting the domain name for `gazel.la` lapse I was faced with
getting a new domain name. And with a new domain name comes new challenges,
like updating the design of this blog with a mobile first design.

While I was at it I also deleted some old posts in which I no longer believe.
Perhaps I was a bit naive when I wrote them.

Other changes I've made:

* Delete facebook comments. I'll change to Disqus some time in the future.
* Delete POST, DELETE and PUT methods on the posts controller <em>[1]</em>
* Delete entries which I no longer believe in or think are silly
* Upgrade rails to `3.2.22`

<em>[1]</em> So, I wrote previously that I want to roll some kind of cert solution
to modifying posts. This actually already exists in the form of the heroku
tool belt configured to use your .ssh private key.

With this I think no one will deface me, so... here's the [code](https://github.com/OryxGazella/blog.frank.soy).
What you're seeing here at the time of writing is what's on the `prod` branch.

I'm not really good at making things look nice, so I got some valuable advice. And yes,
comments such as:

* Eye-cancer
* Geocities throwback
* Affront to decency
* You're obviously trolling us

drove me to try and get something a bit better. Of course in there, there was some constructive criticism too :-)

A big thanks to, in order of feedback given:

* [maneeshchiba](http://maneeshchiba.com/)
* [riggaroo](http://riggaroo.co.za/)
* [jesska](http://jesska.co.za/)


### How do I shot post? ###

![how?](/img/shot_post.jpg)

Instead  of using rails at all to post. I simply generate a SQL script that I
run as follows, while being in my source root linked to my [heroku app](blog-frank-soy.herokuapp.com):

``` bash
cat src/main/resources/db/dev/migration/V2__Test_Data.sql | heroku pg:psql
```

With the file looking something like the following:

``` sql
INSERT INTO posts (
              title,
              body,
              rendered_body,
              created_at,
              updated_at,
              abstract,
              rendered_abstract)
              VALUES (
              'TITLE',
              'BODY',
              'RENDERED',
              '2000-01-01 00:00:00.0000',
              '2000-01-01 00:00:00.0000',
              'ABSTRACT',
              'RENDERED');
```

With the site not dependent on a security key, we can go ahead and open source it.
I'm working on a drop in replacement, written in Kotlin, Spring Boot and Ember, so I suspect this will be the last
time that I make any significant update to the `prod` branch.
You can check that out on the [master](https://github.com/OryxGazella/blog.frank.soy) branch.

While I realise that I can probably achieve what I want using something
like `Jekyll`, I'm using this as my practice area for front-end development. I also have full control over
what I can put in here for when I want to get fancy.

Additionally, my work life has not really given me the opportunity to do a lot of front
end stuff, and I'd still like to keep my hand in it. Not that I'm complaining, though.

Let me know what you think (in the non-existent comments section).
