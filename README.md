HotSwapAgent-IntelliJ-IDEA-plugin
=================================
 
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

1. [~~JRebel~~](http://zeroturnaround.com/software/jrebel/) (too expensive [`$475`](http://zeroturnaround.com/software/jrebel/pricing/) :fearful: per year per user)
2. [~~Spring-Loaded~~](https://github.com/spring-projects/spring-loaded) (cannot work properly with java 8 and `crash JVM` :dizzy_face:)
3. [DCEVM](https://dcevm.github.io/) (free-opensource-really`hot` :grinning:)

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
|5. More info [here](http://www.hotswapagent.org/quick-start)|

### Start with `Hot`SwapAgent
| steps: |  
| ----- | 
|1. Install HotSwapAgent plugin for IntelliJ IDEA.
![plugin](https://cloud.githubusercontent.com/assets/1389501/23871083/4102724e-0829-11e7-93d0-de5f776b31ca.png)|
|2. Enable HotSwapAgent plugin for all configurations.
![enable](https://cloud.githubusercontent.com/assets/1389501/23871159/85dce804-0829-11e7-9ed1-d5986f347b9e.png)|
|3. You will see HOTSWAP AGENT notification in your console in case if setup was correctly done.
![image](https://cloud.githubusercontent.com/assets/1389501/23871016/fbf746d4-0828-11e7-955f-6410d5cced10.png)|
|4. Be sure that you have `"Reload classes after compilation: Always"` ON in IntelliJ IDEA in HotSwap section in your IDEA preferences.
![image](https://cloud.githubusercontent.com/assets/1389501/23870558/12ddd752-0827-11e7-9689-2015c38ebaea.png)|
|5. To re-deploying changed classes in IDEA press keys combination `Command+Shift+F9`|

### Happy:heart: `Hot` Swapping!
| 1. `change` method name & then press `Command+Shift+F9`| 2. `add new` method & then press `Command+Shift+F9` |
| -------- | :------- |
| ![dcevm-hotswap_ok](https://cloud.githubusercontent.com/assets/1389501/23869988/4979098c-0825-11e7-88d2-7ef853ba4110.gif)   | ![dcevm-hotswap_add_ok](https://cloud.githubusercontent.com/assets/1389501/23869987/48797756-0825-11e7-93a6-3691d336a281.gif)    |

### More info: [DCEVM](https://dcevm.github.io/) & [HotswapAgent](http://www.hotswapagent.org/)

### RoadMap:

1. Automate DCEVM installation process inside plugin itself.