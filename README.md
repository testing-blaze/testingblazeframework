<!-- Testing Blaze Automation Solution -->

![TBS logo](documentation/img/project_name.gif)

[![Apache V2 License](http://img.shields.io/badge/license-Apache%20V2-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.testing-blaze/blaze-core.svg)](https://mvnrepository.com/artifact/com.github.testing-blaze/blaze-core)
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

#### Element Processing:
 Every element is received by the framework, and it is evaluated, analyzed and ensured to be properly loaded on the page before any further action is performed. This helps in better stability for all type of applications.
#### Auto Scrolling: 
 Any element which is ready to receive but isn’t in the viewport is automatically scrolled and brought in middle of the page.<br>
#### Auto Frame/Iframe Switching In-out:
 If the element is present inside and iframe then the frames will be automatically switched in and out.
#### Highlighting of elements accessed by automation:
 The elements which are accessed will get highlighted (green for success, red for problem).
#### Auto Tabs Switching:
 If a click action results in opening of any new tab, then the context automatically gets switched to the new tab.
#### Smart Switching to devices:
 Tests written for a computer browser can also be execute on a mobile device browser without any change to tests.
#### AI based Self-Healing of Tests [Authorization Required]: 
 This utility will enable automatic updating of the tests after evaluating the changes to the DOM. This helps in reducing the maintainability of the test’s suite. Integration of this feature with testing blaze will give it a unique edge where we can have a self-healing mechanism working with open source technology set like selenium and cucumber.
#### Auto Dependency Injection:
 Everything is automatically managed by DI. No need to worry about object creation (see example here)
##### Built in cucumber Steps
##### Auto Runner Creation
<!-- USAGE EXAMPLES -->
## Supports

•	Devices: Computers, Mobiles, Tablets, Cloud, Docker <br>
•	Execution Types: Parallel, Sequential, Browser UI, Browser headless<br>
•	Application Types: Browser Based (Angular/HTML), Mobile App Based (Native/Hybrid)<br>
•	Browsers: Chrome, Firefox, Edge, IE, Safari<br>

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
I.amPerforming().click().on(ByUsing.xpath("//"));<br>
I.amPerforming().textInput().in(ByUsing.xpath("//"),inputText);<br>

•	This will perform the click/input on any device.<br>
•	It will also perform certain processing to ensure element is ready to click and also switch to any frame if needed.<br>
•	It is also accompanied with retries in certain situations at own.<br>

#### Interact with selenium, appium abs self healing with single type "ByUsing":
This type gives access to all locator types including selenium , android , ios and self healing.
Example: ByUsing.xpath("//"")

## Using Self healing feature:
•	Get the credentials after authorization.<br>
•	Login to the provided portal for locator management.<br>
•	Use is in your code with ByUsing.healingXpathName(LocatorName).<br>
•	If Dom will change, test will auto heal.<br>
•	Example can be seen on this link.<br>

## Run Your Test:
Open command line/Terminal/IDE. Reach to Project root and execute below as required
##### Standard Command: mvn clean verify -Dtags=@testCaseTag

1-	On Local Browser:<br>
Standard Command + <br>
Choose Browser:<br>
-Ddevice=chrome/firefox/ie/safari

2-	On Jenkins:<br>
Standard Command + <br>
Choose Browser:<br>
-Ddevice=chrome/firefox/ie/safari

3-	On Cloud (SauceLab/BrowserStack):<br>
Standard Command + <br>
Choose Browser:<br>
-Ddevice=chrome/firefox/ie/safari<br>
-Dhub=https://<Sauce userName>:<Sauce key>@ondemand.saucelabs.com:443

4-	On Mobile: [Appium Server will be started and stopped automatically] <br>
Standard Command + <br>
-Dhub=http://IP_For_Appium:Port 		[http://0.0.00:4723]<br>
For Browser:<br>
-Ddevice=android (Chrome is default browser)<br>
For App:<br>
-Ddevice=android<br>
-DappName=appname<br>
Note: Place the mobile app in “mobileApp” folder in project root. Add “appConfig.properties” in same folder with information of appPackage and appActivity for Android.<br>

5-	On Docker: [Docker will be started and stopped automatically]<br>
[Download images and name them as selenium-chrome or selenium-Browser-Name]<br>
Standard Command +<br>
-Ddocker=true<br>
-Dhub=http://IP_For_Docker:Port 		[http://0.0.00:4444]<br>
For Browser:<br>
-Ddevice=android (Chrome is default browser)<br>
For App:<br>
-Ddevice=android<br>
-DappName=appname<br>
->If you want to stop the container after every test then call a method stopDocker in @Before tag.<br>

####	Other Options: 
•	Browser Mode: -DbrowserMode=incognito<br>
•	Switch Environemnts: -Denv=qa <br>
•	Select OS: - -Dplatform=windows/mac -Dversion=10 <br>
•	Headless Mode: -Dheadless=true <br>
•	Parallel Execution: -Dthreads=10/AnyNumber <br>
•	Increase Element Loading Time: -DwaitTime=20 <br>
•	Record Video:  -DrecordVideo=true <br>
•	Enable Screen Shots for all: -DenableScreenShotsForAll=true <br>
•	Run multiple tags =”@test1 or @test2” <br>
•	Skip test/scenario: use @wip on scenario or feature file <br>
•	Enable Complete page Screen Shot: -DenableFullScreenShot=true <br>
•	Use Specific execution Driver =  -DdriverVersion=74 <br>
•	Use Specific execution Browser =  -DbrowserVersion=74 <br>
•	Slow Down Execution Speed = -DslowDownExecution = 1   (it will be considered 1 second) <br>
•	Run a test tag multiple times = -DnumberOfTimesRunTag = 2 <br>
•	Post test results = -DpostTestResults = tfs/jira <br>
•	Enable Screenshots for Soft Assertions failure: -DsoftAssertScreenshot=true <br>

## Cucumber Dynamic Parameterization
#### Saving and retrieving parameters from feature file

Within any step in a feature file you may dynamically adjust the Strings passed to steps at run time.  To do so, you can use either of the following formats:<br>
•	---Key:-:Value--- <br>
•	{Key:Value} <br>

In the section that follows the form {Key:Value} will be used for consistency. But ---Key:-:Value--- has the same functionality in all respects.<br>

1-Adding parameters to locators: <br>

---Key:-:Value--- : <br>

#### Example:
Add below regex in any of your step in feature file to read parameter and get added to xpath automatically<br>
  ---PropertiesFileName:-:ParameterNameInPropertiesFile--- <br>

2-Passing parameters/values for input: <br>

{Key:Value} <br>

By default, “Key” is interpreted as the name of a file with name “Key.properties”, and the value substituted will by the property value from that file named “Value”. For example, consider the following step: <br>
•	I see the text “Value 1: {KPI:Value-1}” <br>

Without dynamic processing, the parameter passed to the Step file would be precisely “Value 1: {KPI:Value-1}. <br>
With dynamic processing, the parameter is modified, and the Step file receives the value “Value 1: <value>”, where <value> is the property with name “Value-1” in the file “KPI.properties”.<br>

In addition to reading from property files, there are several reserved words which can be passed as the Key, which will perform different processing. They are as follows: <br>

•	{SavedValue:ValueKey} - Will substitute the value previously saved in the scenario via a call to “I.amPerforming().propertiesFileOperationsTo().saveValue(ValueKey, theSavedValue)”.  <br>
This works exactly as if a call to “I.amPerforming().propertiesFileOperationsTo()getValue(ValueKey) had been made.  <br>
•	{Username:UserLevel} - Will substitute the username of the user with userLevel “UserLevel”. <br>
“UserLevel” should be the same String that would be passed to the step “I login as ‘userLevel’ user” <br>
•	{Password:UserLevel} - The same as Username, except it substitutes the password of the given user.<br>
•	{Email:UserLevel} - The same as Username, except is substitues the email of the given user.  <br>
Note, for this to work the “getEmail()” method must be added in UsersAndUrl for each individual project. <br>
•	{Date:DateFormat} – Will substitute today’s date in the desired format, like dd/MM/yyyy, or MM/dd/yyyy. <br>
•	{Date:DateFormat::Modifiers} – The same as previous entry, but date can be modified using a semi-colon delimited list of modifiers.  The format for each modifier is “char+int”, “char-int”, or “char=int”. <br>
“char” can be any letter that would be used in DateFormat, and modifies the same part of the date.<br>
(i.e. m=1 sets the minute to 1.  M=1 sets the month to 1.)<br>
Modifiers are processed from left to right. So “d=1,M+1,d-1” will set the day to the first of the month, add one to the month, and subtract 1 from the day, resulting in the last day of the current month.<br>
  
#### Dynamic parameter usage examples
1.	Given a file named “KPI.properties” exists with the following contents:<br>
Value-1=This is the first KPI <br>
Value-2=This is the second KPI <br>
The following substitution can be made: <br>
I see the text “The current KPI is: {KPI:Value-2}” <br>
                      ↓
I see the text “The current KPI is: This is the second KPI” <br>

2.	Given that the following call has been made earlier in the scenario: <br>
I.amPerforming().propertiesFileOperationsTo().saveValue (“BudgetPeriodEndDate”, “10/11/2020”) <br>
The following substitution can be made: <br>
I see the text “The Budget Period end on: {SavedValue:BudgetPeriodEndDate}” <br>
                      ↓<br>
I see the text “The Budget Period end on: 10/11/2020” <br>

3.	Given that the following call will login a user with Username “qwe@qwe.com.qa2” , Password “test@1234”, and Email “qwe@qwe.com”: <br>
I login as “EXE FO” user <br>
The following substitutions can be made: <br>
I see the text “The Username: {Username:EXE FO}” <br>
I see the text “The Password: {Password: EXE FO}” <br>
I see the text “The Email: {Email:EXE FO}”<br>
                      ↓<br>
I see the text “The Username: qwe@qwe.com.qa2” <br>
I see the text “The Password: test@1234” <br>
I see the text “The Email: qwe@qwe.com”<br>

4.	Given that the current date is “10/27/2020”, the following substitutions can be made. <br>
I see the text “Current Date (day first): {Date:dd/MM/yyyy}”<br>
I see the text “Current Date (month first): {Date:MM/dd/yyyy}”<br>
I see the text “Current Date (with time): {Date:dd/MM/yyyy hh:mm:ss}”<br>
I see the text “Last of the Month: {Date:dd/MM/yyyy::d=1;M+1;d-1}”<br>
                     ↓<br>
I see the text “Current Date (day first): 27/10/2020”<br>
I see the text “Current Date (month first): 10/27/2020”<br>
I see the text “Current Date (with time): 27/10/2020 12:34:12”<br>
I see the text “Last of the Month: 10/31/20”<br>

## Misc
#### Setting Global Variable and Accessing it:
TestingBlazeGlobal.setValue(variableName,Value );
TestingBlazeGlobal.getValue(variableName);
#### Turn off element highlighting feature for specific element:
TestingBlazeGlobal.setVariable("highlightElements", "off");<br>
#### Enabling wait for any Processing/In progress fading screen:
TestingBlazeGlobal.setVariable("processingHoldOnScreen",ByUsing.xpath("Xpath of nav page"));

## Sample project
please refer to the [Sample Project](https://github.com/testing-blaze/sample-project.git)<br>

Project Link: [The Testing Blaze Automation Solution](https://github.com/testing-blaze/testingblazeframework)
