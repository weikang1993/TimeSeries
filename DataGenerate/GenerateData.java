import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GenerateData{
  public static void main(String args[]) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
	  DataCore.GetData(1,50,50,200);
	  DataCore.GetData(2,50,50,200);
	  DataCore.GetData(3,50,50,200);
  }
  
}


//核心生成代码
class DataCore{
	public static void GetData(int type,int trainNum,int testNum,int width) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
	 String dataGenerateType="DataGenerate"+Integer.toString(type);
	 String ori_train="ori_train_"+Integer.toString(type);
	 String ori_test="ori_test_"+Integer.toString(type);
	 
	 String reg_train="reg_train_"+Integer.toString(type);
	 String reg_test="reg_test_"+Integer.toString(type);
	 
	 Class dataGenerateString=Class.forName(dataGenerateType);
	 Constructor constructor=dataGenerateString.getConstructor(int.class,int.class,int.class);
	 DataGenerate dataGenerate=(DataGenerate) constructor.newInstance(trainNum,testNum,width);
	 
	 ArrayList<ArrayList<Double>> ori_train_list=dataGenerate.getTrainData();
	 ArrayList<ArrayList<Double>> ori_test_list=dataGenerate.getTestData();
	 
	 ArrayList<ArrayList<Double>> reg_train_list=regTool.reg(ori_train_list);
	 ArrayList<ArrayList<Double>> reg_test_list=regTool.reg(ori_test_list);
			  
	 saveTool.saveFile(ori_train_list, ori_train);
	 saveTool.saveFile(ori_test_list, ori_test);
			  
	 saveTool.saveFile(reg_train_list,reg_train);
	 saveTool.saveFile(reg_test_list,reg_test); 
	}
	
}
//归一化数据的Tool
class regTool{
  public static ArrayList<ArrayList<Double>> reg(ArrayList<ArrayList<Double>> ele){
	  ArrayList<ArrayList<Double>> regList=new ArrayList<ArrayList<Double>>();
	  
	  for(int i=0;i<ele.size();i++){
		double sum=0,avg=0,fangcha=0,biaozhun=0;
		for(int j=1;j<ele.get(i).size();j++) sum=sum+ele.get(i).get(j);
		avg=sum/ele.get(i).size();
		for(int j=1;j<ele.get(i).size();j++)fangcha=fangcha+(ele.get(i).get(j)-avg)*(ele.get(i).get(j)-avg);
	    biaozhun=Math.sqrt(fangcha/ele.get(i).size());
	    ArrayList<Double> temp=new ArrayList<Double>();
	    temp.add(ele.get(i).get(0));
	    for(int j=1;j<ele.get(i).size();j++){
	    	double y=(ele.get(i).get(j)-avg)/biaozhun;
	    	temp.add(y);
	    }
	    regList.add(temp);
	  }
	  return regList;
  }
}


//写文件
class saveTool{
	public static void saveFile(ArrayList<ArrayList<Double>> ele,String filename) throws IOException{
		FileWriter fw=new FileWriter(filename);
	    PrintWriter pw=new PrintWriter(fw);
	    
	    DecimalFormat df=new DecimalFormat("#.##");
		
	    for(int i=0;i<ele.size();i++){
	    //写类标操作
	    	pw.print((ele.get(i).get(0)).intValue());
	    //写值操作
	    	for(int j=1;j<ele.get(i).size();j++){
	    		pw.print(",");
	    		pw.print(df.format(ele.get(i).get(j)));
	    	}
	    	pw.print("\n");
	    }
	   pw.close();
	   fw.close();
	}
}



//数据生成的父类，其他
class DataGenerate{
	ArrayList<ArrayList<Double>> trainData=new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> testData=new ArrayList<ArrayList<Double>>();
	
	public int trainNum;
	public int testNum;
	public int width;
	
	public DataGenerate(int trainNum,int testNum,int width){
		this.trainNum=trainNum;
		this.testNum=testNum;
		this.width=width;
	}
	public ArrayList<ArrayList<Double>> getTrainData(){return null;}
	public ArrayList<ArrayList<Double>> getTestData(){return null;}
}


//按照规则一进行数据生成,生成三类周期性的波形
//分别是正弦波，锯齿波，方波
class DataGenerate1 extends DataGenerate{
	//与该种数据类型相关的参数
	public int zhouqi;
	public int zhengfu;
    
    public DataGenerate1(int trainNum,int testNum,int width){
    	super(trainNum,testNum,width);
		this.zhouqi=(int)(Math.random()*10)+1;
		this.zhengfu=(int)(Math.random()*10)+1;
    }
    

    //生成数据覆盖一个周期
    public ArrayList<ArrayList<Double>> getTrainData(){
        for(int i=0;i<trainNum;i++){
        	//生成的类型
        	int randType=(int)(Math.random()*10)%3;
        	int randStart=(int)(Math.random()*100)+1;  
			
        	//生成数据
            ArrayList<Double> tempList=new ArrayList<Double>();
            tempList.add((double)randType);
            
        	for(int j=randStart;j<randStart+width;j++){
        	 double val=getValue(randType,j,zhouqi,zhengfu);
        	 tempList.add(val);
        	}
        	
        	 trainData.add(tempList);
        }	
    	return trainData;
    }
    
    
    //生成数据覆盖一个周期
    public ArrayList<ArrayList<Double>> getTestData(){
    	for(int i=0;i<testNum;i++){
    		int randType=(int)(Math.random()*10)%3;
			int randStart=(int)(Math.random()*100)+1;
			int randChange=(int)(Math.random()*10)%3;  
			
    		ArrayList<Double> tempList=new ArrayList<Double>();
    		tempList.add((double)randType);
    		
    		for(int j=randStart;j<randStart+width;j++){
    			double val=getValue(randType,j,zhouqi,zhengfu);
				val=randomLineChange(randChange,val);
				tempList.add(val);
    		}
			
			testData.add(tempList);
    	}
    	System.out.println(testData.size());
    	return testData;
    }
    
    
    //计算序列中第i个值
    public double getValue(int type,int i,int zhouqi,int zhengfu){
      double res=0;
      
      switch(type){
      case 0:
    	  //矩形波
    	  if(i%zhouqi>=zhouqi/2) res=0;
    	  else res=zhengfu;
    	  break;
      case 1:
    	  //正弦波
    	  res=zhengfu*Math.sin(zhouqi*i);
    	  break;
      case 2:
    	  //锯齿波
    	  res=i%zhouqi;
    	  break;
      default:
    	  break;      
      }  
      return res; 
    } 
    
    
    //做随机变换
    public double randomLineChange(int type,double value){
    	double res=0;
    	int beishu=((int)Math.random()*10%5+1);
    	int weiyi=((int)Math.random()*10+1);
    	switch(type){
    	case 0:
    		res=value;
    		break;
    	case 1:
    		res=beishu*value;
    		break;
    	case 2:
    		res=value+weiyi;
    		break;
    	default:
    	}
    	return res;
    }
}


//生成一系列的折线
//折线分为5段，每段斜率在-10在10之间随机
//斜率根据大小分为-1到-5，-6到-10，1到5,6到10四种类型
//折线的每一段斜率类型都相同时认为这一段折线为一类
class DataGenerate2 extends DataGenerate{
    public int duanshu;
    ArrayList<ArrayList<Integer>> kList=new ArrayList<ArrayList<Integer>>(); //斜率
	public DataGenerate2(int trainNum,int testNum,int width){
		super(trainNum,testNum,width);
		duanshu=5;  //折线的段数为5
		
		//随机生成4组4*5的斜率
		for(int i=0;i<4;i++){
			ArrayList<Integer> tempList=new ArrayList<Integer>();
			for(int j=0;j<duanshu;j++){
			int randK=(int)(Math.random()*10)%4;
			tempList.add(randK);
			}
			kList.add(tempList);
		}
	}
	
	public ArrayList<ArrayList<Double>> getTrainData(){
		//按照要求生成
		for(int i=0;i<trainNum;i++){
		    //从KList取数据	
			int randType=(int)(Math.random()*10)%4;
			ArrayList<Double> tempList=new ArrayList<Double>();
			tempList.add((double)randType);
			int startPoint=(int)(Math.random()*10);
			int k=0;
			double lastVal=startPoint;
			for(int m=startPoint;m<startPoint+width;m++){
				int duanId=(m-startPoint)/(width/duanshu);
				if((m-startPoint)%(width/duanshu)==0)k=getK(kList.get(randType).get(duanId));
				double val=lastVal+k;
				lastVal=val;
				tempList.add(val);
			}
			
			trainData.add(tempList);
		}
		return trainData;
	}
	
	
	public ArrayList<ArrayList<Double>> getTestData(){
		for(int i=0;i<testNum;i++){
			int randType=(int)(Math.random()*10)%4;
			ArrayList<Double> tempList=new ArrayList<Double>();
			tempList.add((double)randType);
			int startPoint=(int)(Math.random()*10);
			
			int k=0;
			double lastVal=startPoint;
			
			for(int m=startPoint;m<startPoint+width;m++){
				int duanId=(m-startPoint)/(width/duanshu);
				//System.out.println(kList.get(randType).get(duanId));
				if((m-startPoint)%(width/duanshu)==0) k=getK(kList.get(randType).get(duanId));
				double val=lastVal+k;
				lastVal=val;
				tempList.add(val);
			}
			testData.add(tempList);
		}
		return testData;
	}
	
	public int getK(int type){
		int k=0;
		switch(type){
		case 0:
		 k=-((int)(Math.random()*10)%5+1);
		 break;
		case 1:
		 k=-((int)(Math.random()*10)%5+6);
		 break;
		case 2:
		 k=(int)(Math.random()*10%5)+1;
		 break;
		case 3:
		 k=(int)(Math.random()*10%5)+6;
		 break;
		default:
		}
	   return k;
	}
}


//生成一系列折线
//每条折线由5段组成
//斜率分为4类：-1~1，-5~5，-10~10，-15~15组成
//折线同样分为4类，0类：斜率变化在-1~1之间，1类：斜率变化在-5~5之间，2类：斜率变化在-10~10之间，3类：斜率变化在-15~15之间
class DataGenerate3 extends DataGenerate{

	public int duanshu;  
	public DataGenerate3(int trainNum, int testNum, int width) {
		super(trainNum, testNum, width);
		this.duanshu=5;
	}
	
	public ArrayList<ArrayList<Double>> getTrainData(){
		for(int i=0;i<trainNum;i++){
			int randType=(int)(Math.random()*10)%4;
			ArrayList<Double> tempList=new ArrayList<Double>();
			tempList.add((double)randType);
			int startPoint=(int)(Math.random()*10);
			int k=0;
			double lastVal=startPoint;
			
			for(int m=startPoint;m<startPoint+width;m++){
				if((m-startPoint)%(width/duanshu)==0) k=getK(randType);
				double val=lastVal+k;
				lastVal=val;
				tempList.add(val);
			}
			trainData.add(tempList);
		}
			 return trainData;
	}
	
	
	public ArrayList<ArrayList<Double>> getTestData(){
		for(int i=0;i<testNum;i++){
			int randType=(int)(Math.random()*10)%4;
			ArrayList<Double> tempList=new ArrayList<Double>();
			tempList.add((double)randType);
			int startPoint=(int)(Math.random()*10);
			int k=0;
			double lastVal=startPoint;
			
			for(int m=startPoint;m<startPoint+width;m++){
				if((m-startPoint)%(width/duanshu)==0) k=getK(randType);
				double val=lastVal+k;
				lastVal=val;
				tempList.add(val);
			}
			testData.add(tempList);
		}
	        return testData;	
	}
	
	
	public int getK(int type){
		int k=0;
		switch(type){
		case 0:
		 k=1;
		 break;
		case 1:
		 k=(int)(Math.random()*10)%5+1;
		 break;
		case 2:
		 k=(int)(Math.random()*10)%10+1;
	     break;
		case 3:
		 k=(int)(Math.random()*100)%15+1;
		 break;
        default:
		}
		
		if((int)(Math.random()*10)%2 ==0 ) return k;
		else return -k;
	}
	
}
