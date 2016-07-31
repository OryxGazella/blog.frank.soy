---
layout: post
title:  "RIP: The Monolithic Blogpost 2012-2012"
date:   2013-01-10 00:23:00 +0200
categories: blog 
---
Despite what my (small) base of readers may think. I have not abandoned [frank.soy](http://frank.soy). Instead, I have been preparing a "Tech Roundup" of stuff I'm excited about, with bite size examples of each. When I got to AngularJS I knew that I didn't know enough to speak about it with authority... and so I did the [tutorial](http://docs.angularjs.org/tutorial) on and off.

Later, I had to help someone make a website (they were allowed to use a template, and I decided to write this [template](https://github.com/OryxGazella/photography-portfolio).) I took this as an opportunity to implement some things from first principle (like a lightbox) for my own edification.

So finally I get back to "the monolithic blogpost" and I pick up where I left off with the awesome AngularJS tutorial and I see these people use Twitter Bootstrap and I think to myself. 

* Why don't I make gazel.la into a one page application? **[Done]** You can take a look [here](http://gazella-preview.herokuapp.com) at what it will look like. Should work way better for mobile phones. 
* Why don't I investigate distributing all of its assets over a CDN? **[Pending]**
* Why don't I fix some of the frustrations that are keeping me from writing more, like the ability to see what I write as I type? **[Done]** This is the results of another project, written in AngularJS. [Github](https://github.com/OryxGazella/markdown-preview), [Application Instance](http://markdown-blog-preview.herokuapp.com/).

I like to give my readers something to play with so go on and head to the application instance

**Some things to try:**

``` bash
# Heading
## Subheading
```
Gives:

# Heading

## Subheading

Snore! Let's type some code.

``` bash
``` javascript
(function( $ ) {
  $.fn.proclaim = function() {
    alert('JavaScript will never die!');
  };
})( jQuery );
REMOVETHIS```
```
After removing REMOVETHIS yields:

``` javascript
(function( $ ) {
  $.fn.proclaim = function() {
    alert('JavaScript will never die!');
  };
})( jQuery );
```

I haven't minified, combined or compressed any of the assets, so sorry if this takes a long time to load, its normal use case is to run on a server on my machine.

As noted in some of my earlier posts, [frank.soy](http://frank.soy) is written using RedCarpet style MarkDown and Pygments for code highlighting. I tended to put up my works in progress onto a development version for this blog in order to edit my posts and this can become cumbersome, especially when I need to change stuff.

So I'll be splitting my big post into little chunks, each very briefly introducing new technologies that excite me. And with the preview service I can see what I'm doing as I'm doing it.
