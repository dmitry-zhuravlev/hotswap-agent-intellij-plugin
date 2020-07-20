HotSwapAgent-IntelliJ-IDEA-plugin
=================================
![hotswaplogo](https://cloud.githubusercontent.com/assets/1389501/24660351/9682293a-194f-11e7-8b1a-2e193d0b1202.png)
 
`Hot`Swapping
===========

Our work is fun, but we don't want to waste our time waiting when our latest changes will be deployed on application server!

You can reload classes changed during debugging without need to restart the entire application using the HotSwap mechanism `Command+Shift+F9` in IntelliJ IDEA.

![jvm_hotswap_ok](https://cloud.githubusercontent.com/assets/1389501/23869997/4dbc4b3a-0825-11e7-9cf4-272b3570d91d.gif)

`BUT!` At the moment due to original limitations of Java SDK the `Hot`Swapping is possible ONLY if the method body is altered.

In all other cases (like changing method or class signature), the class reload is `impossible` and the corresponding error message appears.

![jvm_hotswap_fail](https://cloud.githubusercontent.com/assets/1389501/23869994/4c238ec8-0825-11e7-96d5-12c2914f9515.gif)


Solution
============

We need to use special java agent together with our JVM to enhance ability of reloading of changed classes. For example: 

1. [~~JRebel~~](http://zeroturnaround.com/software/jrebel/) (too expensive [`$550`](http://zeroturnaround.com/software/jrebel/pricing/) :fearful: per year per user)
2. [~~Spring-Loaded~~](https://github.com/spring-projects/spring-loaded) (cannot work properly with java 8 and `crash JVM` :dizzy_face:, the Spring team propagates the "Restart application on file-change"-approach of [spring-boot-devtools](https://spring.io/blog/2015/06/17/devtools-in-spring-boot-1-3#automatic-restart) for Spring Boot)
3. [~~FakeReplace~~](https://github.com/fakereplace/fakereplace) ([No JDK10+ support yet](https://github.com/fakereplace/fakereplace/issues/33))
4. [~~RelProxy~~](https://github.com/jmarranz/relproxy/) (Dead? No support for JDK8+)
5. [DCEVM](https://dcevm.github.io/) (free-opensource-really`hot` :grinning:)

We will choose: [DCEVM + Hotswap Agent](http://www.hotswapagent.org/) :thumbsup:

DCEVM Installation
==================
| steps: |  
| ----- | 
|1. Download [latest release of DCEVM Java patch](https://github.com/dcevm/dcevm/releases/download/light-jdk8u51%2B3/DCEVM-light-8u51-installer.jar) and launch the installer with sudo(e.g. `sudo java -jar DCEVM-light-8u51-installer.jar`). Currently you need to select correct installer for Java 8.|
|2. Select java installation directory on your disc and press "Install DCEVM as altjvm" button. JDK version `1.8.0_45` is recommended. [download here](http://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase8-2177648.html) 
![DCEVM](https://cloud.githubusercontent.com/assets/1389501/23869992/4aab27ae-0825-11e7-879d-1221cc013228.gif) |
|3. Download [latest release of Hotswap agent jar](https://github.com/HotswapProjects/HotswapAgent/releases), unpack `hotswap-agent.jar` and put it anywhere on your disc. For example: C:\java\hotswap-agent.jar|
|4. Installation is done!|
|5. More info [here](http://www.hotswapagent.org)|

### Start with `Hot`SwapAgent plugin for IntelliJ IDEA.
| steps: |  
| ----- | 
|1. Install HotSwapAgent [plugin](https://plugins.jetbrains.com/plugin/9552-hotswapagent) for IntelliJ IDEA.
![plugin](https://cloud.githubusercontent.com/assets/1389501/24604200/29589bfa-1864-11e7-9dd3-266e08401f24.png)|
|2. Enable HotSwapAgent plugin for all configurations (or one by one).
![enable](https://cloud.githubusercontent.com/assets/1389501/24604139/ed4eba0e-1863-11e7-8dd4-a6b81eb86fb9.png)|
|3. Start the application in DEBUG mode. You will see HOTSWAP AGENT notification in your console in case if setup was correctly done.
![notnotification](https://cloud.githubusercontent.com/assets/1389501/24604444/150f084a-1865-11e7-876b-5615c73bc989.png)|
|4. Be sure that you have `"Reload classes after compilation: Always"` ON in IntelliJ IDEA in HotSwap section in your IDEA preferences.
![image](https://cloud.githubusercontent.com/assets/1389501/23870558/12ddd752-0827-11e7-9689-2015c38ebaea.png)|
|5. To re-deploying changed classes in IDEA press keys combination (: `Command+Shift+F9`/ ⊞: `Ctrl+Shift+F9`) to compile the current file|

:information_source: Instead of compiling the classes manually you can use the [Save Action plug-in](https://github.com/dubreuia/intellij-plugin-save-actions), which can trigger the compilation automatically and thus enabling "hotswap on save".

### Happy:heart: `Hot` Swapping!
||  
| ----- |
|1. `change` method name & then press (: `Command+Shift+F9`/ ⊞: `Ctrl+Shift+F9`)
![change_01](https://cloud.githubusercontent.com/assets/1389501/24606429/56005bc6-186d-11e7-8793-a5ccc7e1b486.gif)|   
|2. `add new` method & then press (: `Command+Shift+F9`/ ⊞: `Ctrl+Shift+F9`)
![change_02](https://cloud.githubusercontent.com/assets/1389501/24606440/5d8d64ec-186d-11e7-970c-f32a1088886b.gif)|


### More info: [DCEVM](https://dcevm.github.io/) & [HotswapAgent](http://www.hotswapagent.org/)
