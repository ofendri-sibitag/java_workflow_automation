# Setting up your Java project

## 1. **Setup your Dockerfile:**

By now, you should have decided what job and instance to use for your project.
Set these in the dockerfile along with a job description of your choice.
Example:
```Dockerfile
ENV JOB_DESCRIPTION="Work time e-mail sent"
ENV JOB = "workflow"
ENV INSTANCE = "timebutler"
```
You can keep "URL" as is for now.  
As mentioned in the [documentation](https://officeasset.atlassian.net/wiki/spaces/~71202096a7ab33509e49eaa93d0f492538368a/pages/430211090/Workflow-Automatisierungsprojekt+einrichten), if you wish to change your main class` name, make sure to change it everywhere in the dockerfile. Example:
```Dockerfile
RUN echo '*/2 * * * * root /usr/local/openjdk-11/bin/java -cp /app Main >> /proc/1/fd/1' >> /etc/crontab
```
It is essential to understand [how CronJobs work](https://kubernetes.io/docs/concepts/workloads/controllers/cron-jobs/) in a Dockerfile. This is how to define the frequency of a CronJob:

```
* * * * * command_to_execute  
- - - - -  
| | | | |  
| | | | +----- Day of the week (0 - 7) (Sunday to Saturday; 0 and 7 both represent Sunday)  
| | | +------- Month (1 - 12)  
| | +--------- Day of the month (1 - 31)  
| +----------- Hour (0 - 23)  
+------------- Minute (0 - 59)
```

Example: A command that runs every 5 minutes would look like this:
```bash
*/5 * * * * echo "Hello World!"
```
A command that runs every 15th of the month at 2pm looks like this:
```bash
*/0 */14 */15 * * echo "Hello World!"
#Every 0th minute, of every 14th hour, of every 15th day, of every month, of every year
```
Edit this as required by your task
## 2. **Run your local Pushgateway**

Either start your local Pushgateway in your Docker Desktop or run this command:
```bash
docker run -d -p 9091:9091 prom/pushgateway  
```
## 3. **Run your code**

Now, everything should be setup for your first build. Run your *setup_project.bat* file through this command in the terminal:
```bash
cd src
./setup_project.bat
```

## 4. **Check that everything is running smoothly**

Upon executing that command, a couple of things should happen:
* A docker container has been created and is running
* Every minute, an up request should be logged into your docker logs and sent to your local Pushgateway
* Every 2 minutes, a counter should increment and be logged into your docker logs and sent to your local Pushgateway
* Your Pushgateway should receive 2 metrics:
    - up with a value of 1
    - my_metric_count with a rising value (every 2 minutes), starting from 1

Make sure you have waited atleast 2 minutes for all of these steps to happen.

## 5. **Have fun with your code**

You can now start writing your own code in my_script.py. Feel free to use as many scripts as you want, as long as you make sure you execute them in my_script.py.  
**DO NOT FORGET** to call ch.send_event() at the end of your script.

## 6. **Changing request URL**

As a last step, you should change the URL for your requests from the local one, to one given to your by the IT-Infrastructure-Team, where data will be read by Promotheus. Make sure to contact them for this step. In order to do this, just change this variable to whatever the IT-Infrastructure-team provided you:
```Dockerfile
ENV URL="http://host.docker.internal:9091"
```