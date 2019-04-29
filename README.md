# Quick Start ![Git](https://img.shields.io/badge/git-any%20version-yellow.svg) ![BBP](https://img.shields.io/badge/bbp-%3E%3D%204.0.0--beta.4-blue.svg)

provide a learn-step-by-step tool.you should launch **BBP** first. 

## Installation

### via command line

```bash
git config --global alias.tour '!for commit in $(git rev-list master --reverse)
do
    git checkout $commit
    read
done
git checkout master'
```

### via text editor 

open`~/.gitconfig`

```bash
[alias]
    tour = !for commit in $(git rev-list master --reverse)\ndo\n    git checkout $commit\n    read\ndone\ngit checkout master
```

## Usage

```bash
git tour
```
 It will start with your initial commit, and advance once each time you hit enter. 

