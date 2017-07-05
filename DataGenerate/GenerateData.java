import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GenerateData{
  public static void main(String args[]) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
	 DataCore.GetData(1, 50, 50, 200);
     
  }
  
}



//�������ɴ���
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
//��һ�����ݵ�Tool
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


//д�ļ�
class saveTool{
	public static void saveFile(ArrayList<ArrayList<Double>> ele,String filename) throws IOException{
		FileWriter fw=new FileWriter(filename);
	    PrintWriter pw=new PrintWriter(fw);
	    
	    DecimalFormat df=new DecimalFormat("#.##");
		
	    for(int i=0;i<ele.size();i++){
	    //д������
	    	//System.out.println(ele.get(i).get(0));
	    	pw.print((ele.get(i).get(0)).intValue());
	    //дֵ����
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


//���չ���һ������������,�������������ԵĲ���
//�ֱ������Ҳ�����ݲ�������
class DataGenerate1 extends DataGenerate{
	//���������������صĲ���
	public int zhouqi;
	public int zhengfu;
    
    public DataGenerate1(int trainNum,int testNum,int width){
    	super(trainNum,testNum,width);
		this.zhouqi=(int)(Math.random()*10)+1;
		this.zhengfu=(int)(Math.random()*10)+1;
    }
    

    //�������ݸ���һ������
    public ArrayList<ArrayList<Double>> getTrainData(){
        for(int i=0;i<trainNum;i++){
        	//���ɵ�����
        	int randType=(int)(Math.random()*10)%3;
        	int randStart=(int)(Math.random()*100)+1;  //
			
        	//��������
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
    
    
    //�������ݸ���һ������
    public ArrayList<ArrayList<Double>> getTestData(){
    	for(int i=0;i<testNum;i++){
    		int randType=(int)(Math.random()*10)%3;
			int randStart=(int)(Math.random()*100)+1;
			int randChange=(int)(Math.random()*10)%3;  
			
			System.out.println(randChange);
    		ArrayList<Double> tempList=new ArrayList<Double>();
    		tempList.add((double)randType);
    		
    		//System.out.println(randType);
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
    
    
    //���������е�i��ֵ
    public double getValue(int type,int i,int zhouqi,int zhengfu){
      double res=0;
      
      switch(type){
      case 0:
    	  //���β�
    	  if(i%zhouqi>=zhouqi/2) res=0;
    	  else res=zhengfu;
    	  break;
      case 1:
    	  //���Ҳ�
    	  res=zhengfu*Math.sin(zhouqi*i);
    	  break;
      case 2:
    	  //��ݲ�
    	  res=i%zhouqi;
    	  break;
      default:
    	  break;      
      }  
      return res; 
    } 
    
    
    //������任
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



//���չ����������������
class DataGenerate2 extends DataGenerate{
	
	//�����ʵ���Լ��ķ���
	public DataGenerate2(int trainNum,int testNum,int width){
		super(trainNum,testNum,width);
	}
	
	public ArrayList<ArrayList<Double>> getTrainData(){
		return trainData;
	}
	
	
	public ArrayList<ArrayList<Double>> geteTestData(){
		return testData;
	}
}