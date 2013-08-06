#!/usr/bin/env python
import git
import subprocess
import os

repo = git.Repo()
dirty = False

if repo.is_dirty(index=False):
    dirty = True
    repo.git.stash('save', '--keep-index')

# generate license headers and return maven output
mvn_output = subprocess.check_output(['mvn', 'clean', 'generate-resources', '-Pgenerate.license.headers'])
directory = os.getcwd()

for line in mvn_output.splitlines():
    if "Updating license header in" in line:
        updated_file = os.path.relpath(line.split()[5], directory)
        repo.index.add([updated_file])

if dirty:
    repo.git.stash('pop')
    