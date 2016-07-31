---
layout: post
title: "Security: art brut"
date:  Tue, Aug 14 2012 at 9:13pm 
---
The one thing one should not use Ruby on Rails for is to build a blog; naturally, I used it to build a blog.

What started off as a casual attempt to build yet another blog ended up in a pretty fun exercise. After purchasing a domain name I was itching to deploy anything and pushed my not yet ready blog into the world. Since it would take some time for anyone to read it or get indexed, I figured that it wouldn't be a problem.

## Problem 1
Blog posts (such as this one) were created with scaffolding, the implication of which is that it has full CRUD (Create, Read, Update, Destroy) functionality for anyone who has access to the restful resources. Here's how I created the Post model.

~~~ console
rails generate scaffold Post title:string body:text
~~~

That's it. Nothing more, nothing less. After running a database migration, you're ready to get posts out into the world, albeit with no formatting. 

The next thing I did was to take a page out of [this guy's](http://danneu.com/posts/9-rails-3-2-markdown-pygments-redcarpet-heroku) book and use GitHub flavoured markdown and pygmentize to author posts.

Obviously I don't want anyone but me editing or creating blog posts (at least not until there's some form of authentication mechanism build into the site).
## Solution
Require some extra form variable in CUD operations, everyone can Read. So yeah, it's dirty, but it gets the job done, quickly. If users don't submit this form variable, redirect them to an error page.

### Create a controller for errors
~~~ console
rails generate controller errors not_allowed
~~~

### Modify the posts_controller to require our token
As a quick fix, I put the following four lines at the start of the create, update and destroy methods of the PostsController.

~~~ ruby
if params[:auth_token] != "SOME_VERY_SECURE_STRING_AS_A_HALF_ASSED_ATTEMPT_TO_SECURE_YOUR_BLOG"
  redirect_to :action => :not_allowed, :controller => :errors
  return
end
~~~

Finally, we'll want to refactor the duplicated code into a [before_filter](http://guides.rubyonrails.org/action_controller_overview.html#filters) and apply them to these three controller methods.

## Problem 2
With this security measure in place, it's difficult to make blog posts.

## Solution
Write blog posts in a file and post it with a script that supplies the security token (using the [heroku SSL](https://agile-cove-1283.herokuapp.com) path to this application to prevent eavesdroppers).
Using the rest-client gem, this is very easy.

### Create a post

~~~ ruby
#!/usr/bin/env ruby
require 'rest-client'

RestClient.post "https://YOUR.URL/posts", {:post => {:title => "YOUR_TITLE", 
        :body => open('YOUR_NEW_POST.md'){|f| f.read}}, 
        :auth_token => "YOUR_SECRET" }
~~~

### Update a post
~~~ ruby
#!/usr/bin/env ruby
require 'rest-client'

RestClient.put "https://YOUR.URL/posts/YOUR_POST_ID", {:post => {:title => "YOUR_TITLE_EDITED", 
        :body => open('YOUR_EDITED_POST.md'){|f| f.read}}, 
        :auth_token => "YOUR_SECRET" }
~~~
### Delete a post
Using a post with a _method form variable.

~~~ ruby
#!/usr/bin/env ruby
require 'rest-client'

RestClient.post "https://YOUR.URL/posts/YOUR_POST_ID", {
        :_method => "delete", 
        :auth_token => "YOUR_SECRET" }
~~~

## The future
As a second stab, creating a public private keypair, loading it into the site and verifying a signature is a better idea... Maybe I'll try that if I'm dateless of a Friday night.
