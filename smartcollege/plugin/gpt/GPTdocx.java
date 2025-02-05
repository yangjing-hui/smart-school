package kxr1.smartcollege.smartcollege.plugin.gpt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import kd.bos.context.RequestContext;
import kd.bos.dataentity.entity.DynamicObject;
import kd.bos.dataentity.entity.DynamicObjectCollection;
import kd.bos.form.gpt.IGPTAction;
import kd.bos.servicehelper.BusinessDataServiceHelper;
import kd.bos.servicehelper.operation.SaveServiceHelper;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import kd.bos.fileservice.FileItem;
import kd.bos.fileservice.FileService;
import kd.bos.fileservice.FileServiceFactory;
import kd.bos.form.gpt.IGPTAction;
import kd.bos.form.gpt.IGPTFormAction;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class GPTdocx implements IGPTAction {
    @Override
    public Map<String, String> invokeAction(String action, Map<String, String> params) {
        Map<String , String> result = new HashMap<>();
        if ("GENERATE_FILE_DOC".equalsIgnoreCase(action)) {
            try {
            //    String fileContent = "aaaaaaaaaaaaa";
                //获取文件字符串
                String fileContent = getFileContent();
                //获取传入参数
          //      String answer_data = params.get("answer_data");
                //将statisticsData转为JSONArray
            //    JSONArray jsonArrayData = JSONArray.parseArray(answer_data);
             //   StringBuilder stringBuilder = new StringBuilder();
                //将数据加入图表
            //    for (int i = 0 ;i<jsonArrayData.size(); i++) {
              //      JSONArray jsonArraySingle = (JSONArray) jsonArrayData.get(i);
                //    if (i==jsonArrayData.size()-1) {
                  //      stringBuilder.append("[&quot;").append(jsonArraySingle.getString(0)).append("&quot;").append(",").append(jsonArraySingle.getString(1)).append(",").append(jsonArraySingle.getString(2)).append("]");
//
  //                  } else {
    //                    stringBuilder.append("[&quot;").append(jsonArraySingle.getString(0)).append("&quot;").append(",").append(jsonArraySingle.getString(1)).append(",").append(jsonArraySingle.getString(2)).append("],");
      //              }
        //        }
           //     String statisticsResult = stringBuilder.toString();
                //替代最后的生成结果
         //       fileContent = fileContent.replace("{{answer_data}}", statisticsResult);

             //   //替代表格中的内容
               // for (int day = 1; day <= 3 ; day++) {
                 //   JSONObject jsonObjectSingle = JSONObject.parseObject(answer_data).getJSONObject("day"+day+"Data");
                   // fileContent = fileContent.replace("{{day"+day+"}}",jsonObjectSingle.getString("day"+day))
                     //       .replace("{{day"+day+"Expect}}",jsonObjectSingle.getInteger("day"+day+"Expect")+"")
                       //     .replace("{{day"+day+"Finish}}",jsonObjectSingle.getInteger("day"+day+"Finish")+"")
                         //   .replace("{{day"+day+"IsOvertime}}",jsonObjectSingle.getString("day"+day+"IsOvertime"));
        //        }

                //替代GPT提示中生成的内容
                fileContent = params.get("answer_data");
              //  fileContent = fileContent.replace("{{threeDayFinishTimeEvaluate}}", params.get("answer_data"));

                //随机生成文件名称
                StringBuilder sb = new StringBuilder();
                for (int i = 1 ; i<=12; i++) {
                    int ascii = 48+(int)(Math.random()*9);
                    char c = (char) ascii;
                    sb.append(c);
                }
                //创建一个临时文件，这里可以直接命名为docx文档
                File targetFile = File.createTempFile(sb.toString(), ".docx");
                if (!targetFile.exists()) {
                    try {
                        targetFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //将字符串写入文件
                byte[] bytes =fileContent.getBytes();
                try {
                    FileOutputStream fos=new FileOutputStream(targetFile);
                    fos.write(bytes);
                    //获取到文件服务器，并将文件上传至文件服务器
                    FileService fs = FileServiceFactory.getAttachmentFileService();
                    String path = "/User/DayEvaluate/"+targetFile.getName();
                    FileItem fi = new FileItem(targetFile.getName(), path, new FileInputStream(targetFile));
                    fi.setCreateNewFileWhenExists(true);
                    //获取到文件路径
                    path= fs.upload(fi);
                    //拼接URL，将最终的URL输出
                    result.put("endUrl", System.getProperty("domain.contextUrl")+"/attachment/download.do?path="+path+"&method=autoJump&title=面试作答报告.docx&iconType=document");

                    targetFile.delete();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 获取文件中的字符串
     * @return
     * @throws Exception
     */
    private static String getFileContent() throws Exception{
        File file = new File("D:/面试作答报告.xml");
        if(!file.exists()){
            return null;
        }
        FileInputStream inputStream = new FileInputStream(file);
        int length = inputStream.available();
        byte bytes[] = new byte[length];
        inputStream.read(bytes);
        inputStream.close();
        String str =new String(bytes, StandardCharsets.UTF_8);
        return str ;
    }
}
