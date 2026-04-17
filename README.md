# ICS4U-Simulation-project
## Tools

> This project is developed with [Greenfoot](https://www.greenfoot.org/door)  
> I suggest using [IntelliJ IDEA](https://www.jetbrains.com/idea/) as the main IDE for this project. From my experience, it is better to use IntelliJ IDEA when the project gets bigger. You will appreciate its LSP server and autocomplete.

## Development Git Workflow

Follow this workflow every time you start working on the project. It keeps your local branch up to date and reduces merge conflicts.

### Before You Start Development: Rebase Your Branch

#### Bash Method

1. Open the project in your terminal.
2. Make sure you are on your own working branch:

```bash
git branch
```

3. If you are not on your branch, switch to it:

```bash
git switch your-branch-name
```

4. Download the newest changes from GitHub:

```bash
git fetch origin
```

5. Rebase your branch on top of the latest main branch:

```bash
git rebase origin/main
```

6. If the rebase finishes successfully, start coding.

If Git reports a conflict during the rebase, resolve the conflict first, then continue the rebase:

```bash
git rebase --continue
```

If something goes wrong and you want to cancel the rebase:

```bash
git rebase --abort
```

#### GitHub Desktop Method

1. Open GitHub Desktop.
2. Select this repository.
3. Use the branch dropdown at the top to switch to your own working branch.
4. Click `Fetch origin` to download the newest changes from GitHub.
5. Open the `Branch` menu.
6. Click `Rebase Current Branch...`.
7. Choose `main` as the branch you want to rebase onto.
8. Click `Start rebase`.
9. If the rebase finishes successfully, start coding.

If GitHub Desktop reports conflicts during the rebase, resolve the conflicts first. After all conflicts are resolved, return to GitHub Desktop and continue the rebase.

### After Development: Commit and Push

#### Bash Method

1. Check which files you changed:

```bash
git status
```

2. Add the files you want to commit:

```bash
git add .
```

3. Create a clear commit message:

```bash
git commit -m "Describe what you changed"
```

Good commit messages should be short but specific. For example:

```bash
git commit -m "Add player movement controls"
git commit -m "Fix collision detection bug"
git commit -m "Update simulation object sprites"
```

4. Push your branch to GitHub:

```bash
git push origin your-branch-name
```

If you rebased a branch that was already pushed before, Git may ask you to force push. Use:

```bash
git push --force-with-lease origin your-branch-name
```

Use `--force-with-lease` instead of `--force` because it is safer and helps avoid overwriting someone else's work.

#### GitHub Desktop Method

1. Open GitHub Desktop.
2. Select this repository.
3. Make sure you are on your own working branch.
4. Go to the `Changes` tab.
5. Review the files you changed.
6. Select the files you want to include in the commit.
7. Write a short commit message in the `Summary` box.
8. Add more details in the `Description` box if needed.
9. Click `Commit to your-branch-name`.
10. Click `Push origin` to upload your branch to GitHub.

If you rebased a branch that was already pushed before, GitHub Desktop may show a force push option. Only use it on your own working branch, and make sure nobody else is using that branch.

### Resolving Merge Conflicts in GitHub Desktop

Merge conflicts happen when two people edit the same part of a file. Git does not know which version to keep, so you must choose manually.

1. Open GitHub Desktop.
2. Select this repository.
3. If GitHub Desktop shows that there are conflicts, click `View conflicts`.
4. Click each conflicted file.
5. Open the file in your editor.
6. Look for conflict markers like this:

```text
<<<<<<< HEAD
Your version of the code
=======
The other version of the code
>>>>>>> branch-name
```

7. Edit the file so it contains the final correct code.
8. Delete all conflict markers:

```text
<<<<<<< HEAD
=======
>>>>>>> branch-name
```

9. Save the file.
10. Go back to GitHub Desktop.
11. Mark the conflict as resolved.
12. Repeat this process for every conflicted file.
13. After all conflicts are resolved, continue the merge or rebase in GitHub Desktop.
14. Commit the resolved changes if GitHub Desktop asks you to.
15. Push your branch.

After resolving conflicts, always run or open the project again to make sure the code still works.
