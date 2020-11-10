<!-- Testing Blaze Automation Solution -->

![TBS logo](documentation/img/project_name.gif)

[![Apache V2 License](http://img.shields.io/badge/license-Apache%20V2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.testing-blaze/blaze-core.svg)](https://repo1.maven.org/maven2/com/github/testing-blze/)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-ff69b4.svg)](https://github.com/testing-blaze/testingblaze/pulls)
[![GitHub issues open](https://img.shields.io/github/issues/network-tools/shconfparser.svg)](https://github.com/testing-blaze/testingblaze/issues)

<!-- PROJECT Information -->
### An Intelligent BDD Test Automation Framework - 'Let the tests heal at own'  
---

<!-- ABOUT THE PROJECT -->
## About The Project

The Testing Blaze Automation Solution brings the universe of open source technologies embedded and architectured in the smartest and most innovative ways to make the automated testing experience easy , interactable and flakiless. 

The solution offers self healing of  tests, intelligent processing , automatic switching to contexts, parameterized integration with cucumber and various other out of box utilities to ensure maximum stability , fast track automation and less maintenance efforts. 


### Built With
* [Java](https://www.java.com/en/)
* [Cucumber](https://cucumber.io/)
* [Selenium](https://www.selenium.dev/)
* [Appium](http://appium.io/)
## Integrated With 
Self healing module which is built with
* [Python](https://www.python.org/)
* [Django](https://www.djangoproject.com/)

<!-- GETTING STARTED -->
## Getting Started

Setting up the project is very simple and does not require any special configurations. You can simply clone a sample project and start working with it.

### Installation

1. Java 11
2. Maven
3. Appium Server (For mobile testing only)

<!-- Details -->
## Key Features
Intelligent Processing for Test Execution Stability & Agility

1-Element Processing: Every element is received by the framework, and it is evaluated, analyzed and ensured to be properly loaded on the page before any further action is performed. This helps in better stability for all type of applications.
2-Auto Scrolling: Any element which is ready to receive but isn’t in the viewport is automatically scrolled and brought in middle of the page.
3-Auto Frame/Iframe Switching In-out: If the element is present inside and iframe then the frames will be automatically switched in and out.
4-Highlighting of elements accessed by automation: The elements which are accessed will get highlighted (green for success, red for problem).
5-Auto Tabs Switching: If a click action results in opening of any new tab, then the context automatically gets switched to the new tab.
6-Smart Switching to devices: Tests written for a computer browser can also be execute on a mobile device browser without any change to tests.
7-AI based Self-Healing of Tests [Not Open Source] This utility will enable automatic updating of the tests after evaluating the changes to the DOM. This helps in reducing the maintainability of the test’s suite. Integration of this feature with testing blaze will give it a unique edge where we can have a self-healing mechanism working with open source technology set like selenium and cucumber. 

<!-- USAGE EXAMPLES -->
## Supports

•	Devices: Computers, Mobiles, Tablets, Cloud, Docker 
•	Execution Types: Parallel, Sequential, Browser UI, Browser headless
•	Application Types: Browser Based (Angular/HTML), Mobile App Based (Native/Hybrid)
•	Browsers: Chrome, Firefox, Edge, IE, Safari

<!-- USAGE EXAMPLES -->
## How to use it ?

The framework offers a gherkin style of code writing which makes each line of code readable and it also provides access to all type of devices,libraries from a single line
of code.

All libraries can be accessed in any Java class using: I.amPerforming() <br>
•	Reach out to your page object class and write I.amPerforming() <br>
•	Each library has relevant utilities inside it to access and use.<br>
•	Each library method will show description of what it does.<br>
•	The code is human readable.<br>
#### How to click / text input?
I.amPerforming().click().on(ByUsing.xpath("//"));
I.amPerforming().textInput().in(ByUsing.xpath("//"),inputText);

•	This will perform the click/input on any device.
•	It will also perform certain processing to ensure element is ready to click and also switch to any frame if needed.
•	It is also accompanied with retries in certain situations at own.

## Run Your Test:
Open command line/Terminal/IDE. Reach to Project root and execute below as required
##### Standard Command: mvn clean verify -Dtags=@testCaseTag

1-	On Local Browser:
Standard Command + 
Choose Browser:
-Ddevice=chrome/firefox/ie/safari

2-	On Jenkins:
Standard Command + 
Choose Browser:
-Ddevice=chrome/firefox/ie/safari

3-	On Cloud (SauceLab/BrowserStack):
Standard Command + 
Choose Browser:
-Ddevice=chrome/firefox/ie/safari
-Dhub=https://<Sauce userName>:<Sauce key>@ondemand.saucelabs.com:443

4-	On Mobile: [Appium Server will be started and stopped automatically]
Standard Command + 
-Dhub=http://IP_For_Appium:Port 		[http://0.0.00:4723]
For Browser:
-Ddevice=android (Chrome is default browser)
For App:
-Ddevice=android
-DappName=appname
Note: Place the mobile app in “mobileApp” folder in project root. Add “appConfig.properties” in same folder with information of appPackage and appActivity for Android.

5-	On Docker: [Docker will be started and stopped automatically]
[Download images and name them as selenium-chrome or selenium-Browser-Name]
Standard Command +
-Ddocker=true
-Dhub=http://IP_For_Docker:Port 		[http://0.0.00:4444]
For Browser:
-Ddevice=android (Chrome is default browser)
For App:
-Ddevice=android
-DappName=appname
->If you want to stop the container after every test then call a method stopDocker in @Before tag.

####	Other Options: 
•	Browser Mode: -DbrowserMode=incognito
•	Switch Environemnts: -Denv=qa 
•	Select OS: - -Dplatform=windows/mac -Dversion=10
•	Headless Mode: -Dheadless=true 
•	Parallel Execution: -Dthreads=10/AnyNumber
•	Increase Element Loading Time: -DwaitTime=20
•	Record Video:  -DrecordVideo=true
•	Enable Screen Shots for all: -DenableScreenShotsForAll=true
•	Run multiple tags =”@test1 or @test2”
•	Skip test/scenario: use @wip on scenario or feature file
•	Enable Complete page Screen Shot: -DenableFullScreenShot=true
•	Use Specific execution Driver =  -DdriverVersion=74
•	Use Specific execution Browser =  -DbrowserVersion=74
•	Slow Down Execution Speed = -DslowDownExecution = 1   (it will be considered 1 second)
•	Run a test tag multiple times = -DnumberOfTimesRunTag = 2
•	Post test results = -DpostTestResults = tfs/jira
•	Enable Screenshots for Soft Assertions failure: -DsoftAssertScreenshot=true

## Cucumber Dynamic Parameterization
####Saving and retrieving parameters from feature file

Within any step in a feature file you may dynamically adjust the Strings passed to steps at run time.  To do so, you can use either of the following formats:
•	---Key:-:Value---
•	{Key:Value}

In the section that follows the form {Key:Value} will be used for consistency. But ---Key:-:Value--- has the same functionality in all respects.

1-Adding parameters to locators:

---Key:-:Value--- : 

Example:
Add below regex in any of your step in feature file to read parameter and get added to xpath automatically
  ---PropertiesFileName:-:ParameterNameInPropertiesFile---

2-Passing parameters/values for input:

{Key:Value}

By default, “Key” is interpreted as the name of a file with name “Key.properties”, and the value substituted will by the property value from that file named “Value”. For example, consider the following step:
•	I see the text “Value 1: {KPI:Value-1}”

Without dynamic processing, the parameter passed to the Step file would be precisely “Value 1: {KPI:Value-1}.
With dynamic processing, the parameter is modified, and the Step file receives the value “Value 1: <value>”, where <value> is the property with name “Value-1” in the file “KPI.properties”.

In addition to reading from property files, there are several reserved words which can be passed as the Key, which will perform different processing. They are as follows:

•	{SavedValue:ValueKey} - Will substitute the value previously saved in the scenario via a call to “I.amPerforming().propertiesFileOperationsTo().saveValue(ValueKey, theSavedValue)”.  
This works exactly as if a call to “I.amPerforming().propertiesFileOperationsTo()getValue(ValueKey) had been made.  
•	{Username:UserLevel} - Will substitute the username of the user with userLevel “UserLevel”.
“UserLevel” should be the same String that would be passed to the step “I login as ‘userLevel’ user”
•	{Password:UserLevel} - The same as Username, except it substitutes the password of the given user.
•	{Email:UserLevel} - The same as Username, except is substitues the email of the given user.  
Note, for this to work the “getEmail()” method must be added in UsersAndUrl for each individual project.
•	{Date:DateFormat} – Will substitute today’s date in the desired format, like dd/MM/yyyy, or MM/dd/yyyy.
•	{Date:DateFormat::Modifiers} – The same as previous entry, but date can be modified using a semi-colon delimited list of modifiers.  The format for each modifier is “char+int”, “char-int”, or “char=int”.
“char” can be any letter that would be used in DateFormat, and modifies the same part of the date.
(i.e. m=1 sets the minute to 1.  M=1 sets the month to 1.)
Modifiers are processed from left to right. So “d=1,M+1,d-1” will set the day to the first of the month, add one to the month, and subtract 1 from the day, resulting in the last day of the current month.
  
#### Dynamic parameter usage examples
1.	Given a file named “KPI.properties” exists with the following contents:
Value-1=This is the first KPI
Value-2=This is the second KPI
The following substitution can be made:
I see the text “The current KPI is: {KPI:Value-2}”
                      ↓
I see the text “The current KPI is: This is the second KPI”

2.	Given that the following call has been made earlier in the scenario:
I.amPerforming().propertiesFileOperationsTo().saveValue (“BudgetPeriodEndDate”, “10/11/2020”)
The following substitution can be made:
I see the text “The Budget Period end on: {SavedValue:BudgetPeriodEndDate}”
                      ↓
I see the text “The Budget Period end on: 10/11/2020”

3.	Given that the following call will login a user with Username “qwe@qwe.com.qa2” , Password “test@1234”, and Email “qwe@qwe.com”:
I login as “EXE FO” user
The following substitutions can be made:
I see the text “The Username: {Username:EXE FO}”
I see the text “The Password: {Password: EXE FO}”
I see the text “The Email: {Email:EXE FO}”
                      ↓
I see the text “The Username: qwe@qwe.com.qa2”
I see the text “The Password: test@1234”
I see the text “The Email: qwe@qwe.com”

4.	Given that the current date is “10/27/2020”, the following substitutions can be made.
I see the text “Current Date (day first): {Date:dd/MM/yyyy}”
I see the text “Current Date (month first): {Date:MM/dd/yyyy}”
I see the text “Current Date (with time): {Date:dd/MM/yyyy hh:mm:ss}”
I see the text “Last of the Month: {Date:dd/MM/yyyy::d=1;M+1;d-1}”
                     ↓
I see the text “Current Date (day first): 27/10/2020”
I see the text “Current Date (month first): 10/27/2020”
I see the text “Current Date (with time): 27/10/2020 12:34:12”
I see the text “Last of the Month: 10/31/20”

## Misc
Setting Global Variable and Accessing it:
TestingBlazeGlobal.setValue(variableName,Value );
TestingBlazeGlobal.getValue(variableName);

Turn off element highlighting feature for specific element:
TestingBlazeGlobal.setVariable("highlightElements", "off");

## Sample project

_For more examples, please refer to the [Documentation](https://example.com)_

Project Link: [https://github.com/testing-blaze/testingblaze](https://github.com/testing-blaze/testingblaze)
