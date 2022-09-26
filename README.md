## Iteration 2 - CSC 2310
---
### Project Description
***

- Consume data from webservice (API url) using a personalized key
- Interpret the data
- Output these interpretations using several webservices (13)

More clarified instructions -- [PDF here](https://elearn.tntech.edu/d2l/lms/dropbox/user/folder_submit_files.d2l?db=8041789&grpid=0&isprv=0&bp=0&ou=8948309 "Iteration 2 link")

### Directory Descriptions
***

- `src/main` - Where the services and the object classes are. `ServiceBridge` is the class that consumes the web services and then interprets the information from it into other services. It is the class that utilizes the other "POJO" classes in the `edu.tntech.csc2310.dashboard.data` package such as `Faculty`. `Spike` is the driver for the entire program that launches the services as SpringBoot webservices.
- `src/test` - Where the services in `ServiceBridge` are tested; both happy and unhappy paths are evaluated.

### Compilation instructions
***

- First, enter the `mvn clean` command in the terminal to refresh the code from last execution.
- Second, enter the `mvn compile` command in the terminal to actually compile the code.
- Third, enter the `mvn package` command to compile all of the files in the package together at the same time.
- Finally, if you want to run the program and launch the springboot services, enter the `./mvnw spring-boot:run` in the terminal.

### Development instructions
***

#### To Clone the Repo:

- To clone the repository, go into your terminal and `cd` into the directory that you want the repository in.
- Next, go to the gitlab project's homepage and locate the "clone" button.
- Click it and it the "Clone with https" option
- Next, in you terminal, enter the command `git clone` and then **Ctrl-V** the https address into the terminal after that command
- Once you have seen the `complete` line after all of the `download` lines, your git repository has been successfully downloaded!

####  Loading Project Into IntelliJ

- First, at the top of the screen in IntelliJ, locate the **File** tab.
- Under the **File** tab, hit **Open**. Locate the directory in which the repository you downloaded is in and select it.
- Next, to Configure the SDK, go to **Project Structure** under the **File Tab**
- A new window will open, and locate and hit the **Project Settings** button.
- Under **Project Settings**, locate the **Project** tab. 
- In the **Project** window, locate and click the **SDK** tab. Select **11 - Eclipse Terminum version 11.0.13**. If you cannot find this SDK, then click **Download JDK** tab and in the list, find JDK 11 and click download. Select in once it downloads, and then in the **Project** window, hit the **Apply** and then hit the **Ok** window to exit the window. The SDK has now been configured
