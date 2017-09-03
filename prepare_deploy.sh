#!/usr/bin/env bash
git clean -dfx
mkdir -p src/assets/html
cd src/jekyll
bundle exec jekyll build
mv _site/* ../assets/html
cd ../..
rm .gitignore
git add .
git commit -m "Add static files"
