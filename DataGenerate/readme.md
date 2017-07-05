# 关于dataGenerate说明
- 概要
  - DataCore  
  根据不同传入参数生成相应数据生成类,该类不需要做更改    
  type:种类  
  trainNum:生成训练数据的条目  
  testNum:生成测试数据的条目  
  width:生成数据的宽度  
  
  - regTool  
  该类主要用于数据的正规化操作，该类不需做任何更改，也无需调用，在DataCore中已调用  
  
  - saveTool  
  该类主要用于保存数据操作，该类不需要做任何更改，也无需调用，在DataCore中已调用  
  
  - DataGenerate  
  模拟数据生成类的父类，主要用来规范构造函数，以及要求子类提供两个成员函数getTrainData(),getTestData().getTrainData()是用于提供训练数据集，getTestData()是用于提供测试数据集。  
  ArrayList< ArrayList < Double > > trainData为训练数据集  
  ArrayList< ArrayList < Double > > testData为测试数据集
 
- 使用
  - 新建类DataGenerate+num
  新建类的类名必须满足字符串"DataGenerate"+数字num,这里的数字num即为该种模拟数据的type，作为该种模拟数据的标识，因此应保证num未出现过（呃呃呃）.例如DataGenerate1,DataGenerate4  
  该类必须继承自DataGenerate类
    
  -  类中方法实现  
  在该类中可以增加相应的成员变量，成员函数，按照自己的意图生成模拟数据（主要是实现getTrainData()与getTestData()两个方法），分为训练数据，测试数据两类。
  
  - 生成调用  
  调用的形式很简单，只要在入口函数中public static void main(String args[])进行调用即可，调用的形式为：DataCore(type,trainNum,testNum,width).  
  
 -  在该java的工程目录下，将会生成4个文件，后缀均为type-num，分别对应着原始的训练数据，原始测试数据，规范化后的训练数据，规划化的测试数据。
 -  简单的来说，这段代码就是干了规范化与保存文件的操作，核心模拟数据模拟的代码需要自己实现相关方法。在这段代码中提供了一个正弦波，矩形波，锯齿波的随机生成的样例，其类名为DataGenerate1。
