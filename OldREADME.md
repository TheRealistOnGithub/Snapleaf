# CISC275-project-template

Note: to run with gradle you must use the directory structure `src/main/java/` and `src/main/resources/`. So you can have multiple projects in this repo. So it could look like `project1/src/main/java` and `project2/src/main/java`. Just make sure to place a `build.gradle` file inside each of the projects root under `project1/` and `project2`.

# Things to place in README and repo
- UMLs
- everything related to your project
- rename this README file and create another one named `README.md` that describes your project in detail. Update it as your project progresses

### Github Actions (Optional but good thing to have)

This is a great feature that will allow us to compile and run our code on Github.com. We could even run unit tests -- see lab1 `build.gradle`

Note: We only compile JavaFX code because it needs a display to run which servers don't have

The `yml` or yaml file in `.github/workflows/gradle.yml` specifies when, where, and how to compile/run. Github looks for a yml file in `.github/workflows` so it can be named anything you want.

In `gradle.yaml`, we specify the OS of the server and what Java version we want. We also specify the command we want the server to run once all the required software is installed.

To run JavaFx, we use Gradle(build automation tool). Gradle needs `build.gradle` where we specify the JavaFX version, the modules we need, and our main class.

#### Note: gradle needs our main class to be located at `src/main/java/` and all the classes main needs relative to that path. And we put any resources we use at `src/main/resources/`

Note that this is how we will setup all projects going forward. Especially your main projects.

Even with all these changes, you should still be able to import your projects into Eclipse like you normally do.

#### Why run code on Github actions?

You can configure it to automatically compile/run your code on every push. It should make it easy to see when/where your project breaks

Once you do this you can see your code compiling under the Actions tab after each push. Some you may have to enable Actions and then make a push.  

See [documentation](https://docs.github.com/en/free-pro-team@latest/actions) on Github actions
