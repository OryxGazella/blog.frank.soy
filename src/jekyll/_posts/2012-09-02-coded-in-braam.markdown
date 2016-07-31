---
layout: post
title:  "Coded in Braam: September 2012"
date:   2012-09-02 19:51:00 +0200
categories: thoughtworks tdd 
permalink: /posts/:slug.html
---
On Saturday the first of September 2012 I spent the first part of a very lovely spring day at the ThoughtWorks South Africa offices in Braamfontein taking part in something called [CodedInBraam](http://www.meetup.com/CodedInBraam/). Coming into the offices I was greeted by ThoughtWorkers [Rachel](http://join.thoughtworks.com/profiles/rachel-laycock) and [Brain](http://join.thoughtworks.com/profiles/brain-leke) who immediately made me feel at ease. 

After the last few attendees joined [David Reed](http://join.thoughtworks.com/profiles/david-reed) gave a talk about the basics of test driven development. He then showed what we would be working on, a JavaScript Space Invaders clone with an existing set of jasmine specs and an easy way to edit them using a web interface.

They then asked us to form a line based on our experience with test driven development. I put myself somewhere in the middle, unable to commit to anything. I was lucky enough to be paired up with Brain. 

After the session I was pleasantly surprised to find out that [Scott Robinson](http://join.thoughtworks.com/profiles/scott-robinson) open sourced the whole [project](https://github.com/quad/testinvaders).

## Infectious culture
So I'm pretty excited to do two small, but potentially revolutionary things in my day-to-day. In my current project (sitting at 11 months at the time of writing) we were one of the first teams in the Johannesburg office to really put investment into doing test driven development (and some pair coding). We have a code coverage of about 83% and it's something the team and I can be very proud of. We had a senior member who was part of the team who really mentored us in the practice of TDD; to the rest of the team, TDD was a fairly foreign concept.

So the first thing that Brain did was to connect another monitor to his laptop, complete with a keyboard and a mouse. That'd never have occurred to me, and doing that for pair programming makes sense. It makes a back and forth very easy. I am taking a spare monitor from home to work ASAP.

Secondly, as we got into things, Brain talked about ping pong programming ([P3](http://www.stickyminds.com/s.asp?F=S9101_COL_2)). The basics are as follows:

1. Dev one writes a failing test.
2. Dev two writes just enough code to make the test pass.
3. Dev two writes a failing test.
4. Dev one implements code.
5. Goto step 1.

The normal classical steps in TDD still apply.

1. Write test.
2. Write just enough code to get the test to pass.
3. Refactor to get rid of any code duplication (this includes duplication between implementation and test).

## What we did

Starting from the normal example as shown on [testinvaders](http://testinvaders.heroku.com) we did the following:

* Made the tank's bullets kill the invaders.
* Stopped the invaders' bullets from killing one another.
* Made the invaders' bullets kill the tank.
* Developed the concept of "lives" on the tank.
* Stopped the game when the tank is out of lives.

<input id="reset_game" type="button" value="reset">

<iframe id="space_invaders_game" style="width:100%;height: 600px;" src="/space_invaders.html"></iframe>

I'd like to continue where Brain and I left off back at the office. I can't wait to share this with my colleagues! Thanks ThoughtWorks for hosting a really cool event. I hope to attend CodedInBraam in October. In November I'll be chilling in Buenos Aires.

<script type="text/javascript">
$('#reset_game').on('click', function(){
	var currentSrc = $("#space_invaders_game").attr("src");
	$('#space_invaders_game').attr("src", currentSrc);
});
</script>
