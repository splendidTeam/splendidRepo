package com.baozun.nebula.utils.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * 图像操作
 * 
 * @author justin
 *
 */
public class ImageOpeartion{

    private static final Logger log        = LoggerFactory.getLogger(ImageOpeartion.class);

    //切换生成略缩图类型
    @SuppressWarnings("unused")
    private static String       reduceType = "BK";

    /**
     * 图像缩放 jpg格式 比例失调时，会剪切部分
     * 
     * @param imgsrc
     *            :原图片文件路径
     * @param imgdist
     *            :生成的缩略图片文件路径
     * @param widthdist
     *            :生成图片的宽度
     * @param heightdist
     *            :生成图片的高度
     */
    public static void reduceImgCut(File srcfile,String imgdist,int widthdist,int heightdist){
        try{
            if (!srcfile.exists()){
                return;
            }
            Image src = ImageIO.read(srcfile);

            int srcWidth = src.getWidth(null);
            int srcHeight = src.getHeight(null);

            int newWidth = widthdist; //截取后的width

            int newHeight = heightdist; //截图后的Height

            int left = 0; //截取图的x值

            int top = 0; //载取图的y值

            float wscale = (float) widthdist / srcWidth;

            float hscale = (float) heightdist / srcHeight;

            if (widthdist != heightdist){ //如果略缩图的宽大于高

                if (wscale < hscale){ //宽比例比较小

                    top = 0;

                    newWidth = (int) ((float) widthdist * srcHeight / heightdist);

                    left = (int) (((float) srcWidth - newWidth) / 2 * hscale);

                    newWidth = (int) (hscale * srcWidth);

                }

                else{
                    left = 0;

                    newHeight = (int) ((float) heightdist * srcWidth / widthdist);

                    top = (int) (((float) srcHeight - newHeight) / 2 * wscale);

                    newHeight = (int) (wscale * srcHeight);

                }

            }

            else{
                int gap = srcWidth - srcHeight; //高与宽的差距

                if (gap > 0){ //如果宽比高大
                    left = (int) ((float) gap / 2 * hscale);
                    newWidth = (int) (hscale * srcWidth);

                }else if (gap < 0){ //如果高比宽大
                    top = -(int) ((float) gap / 2 * wscale);
                    newHeight = (int) (wscale * srcHeight);

                }

            }

            BufferedImage tag = new BufferedImage(widthdist, heightdist, BufferedImage.TYPE_INT_RGB);
            /*
             * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的
             * 优先级比速度高 生成的图片质量比较好 但速度慢
             */
            tag.getGraphics().drawImage(src.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), -left, -top, null);

            FileOutputStream out = new FileOutputStream(imgdist);

            ImageIO.write(tag, "jpg", out);

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    /**
     * 生成略缩图，高宽度失调时，不会裁剪,会修改相关的高宽度
     * 
     * @param imgsrc
     *            当前图片的路径
     * @param imgdist
     *            生成图片的路径
     * @param widthdist
     *            略缩图的宽度
     * @param heightdist
     *            略缩图的高度
     */
    public static void reduceImgMWH(File srcfile,String imgdist,int widthdist,int heightdist){
        try{
            if (!srcfile.exists()){
                return;
            }
            Image src = ImageIO.read(srcfile);

            int srcWidth = src.getWidth(null);
            int srcHeight = src.getHeight(null);

            int newWidth = widthdist; //欲生成图片的宽

            int newHeight = heightdist; //欲生成图片的高

            float cale = 0f; //比例

            if (srcWidth > srcHeight){ //如果源图的宽大于高

                cale = widthdist * 1f / srcWidth;

                newHeight = (new Double(srcHeight * cale)).intValue();

                if (newHeight > heightdist){
                    cale = heightdist * 1f / newHeight;
                    newWidth = (new Double(newWidth * cale)).intValue();
                    newHeight = heightdist;
                }

            }else{ //如果源图的高大于宽
                cale = heightdist * 1f / srcHeight;

                newWidth = (new Double(srcWidth * cale)).intValue();

                if (newWidth > widthdist){
                    cale = widthdist * 1f / newWidth;
                    newHeight = (new Double(newHeight * cale)).intValue();
                    newWidth = widthdist;
                }
            }

            BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            /*
             * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的
             * 优先级比速度高 生成的图片质量比较好 但速度慢
             */
            tag.getGraphics().drawImage(src.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);

            FileOutputStream out = new FileOutputStream(imgdist);
            
            ImageIO.write(tag, "jpg", out);

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    /**
     * 生成略缩图，针对PNG进行处理(透明背景)！
     * 
     * @param imgsrc
     *            当前图片的路径
     * @param imgdist
     *            生成图片的路径
     * @param widthdist
     *            略缩图的宽度
     * @param heightdist
     *            略缩图的高度
     */
    public static void reduceImgWhilePNG(File srcfile,String imgdist,int widthdist,int heightdist){
        try{
            if (!srcfile.exists()){
                return;
            }
            Image src = ImageIO.read(srcfile);

            int srcWidth = src.getWidth(null);
            int srcHeight = src.getHeight(null);

            int newWidth = widthdist; //欲生成图片的宽

            int newHeight = heightdist; //欲生成图片的高

            float cale = 0f; //比例

            if (srcWidth > srcHeight){ //如果源图的宽大于高

                cale = widthdist * 1f / srcWidth;

                newHeight = (new Double(srcHeight * cale)).intValue();

                if (newHeight > heightdist){
                    cale = heightdist * 1f / newHeight;
                    newWidth = (new Double(newWidth * cale)).intValue();
                    newHeight = heightdist;
                }

            }else{ //如果源图的高大于宽
                cale = heightdist * 1f / srcHeight;

                newWidth = (new Double(srcWidth * cale)).intValue();

                if (newWidth > widthdist){
                    cale = widthdist * 1f / newWidth;
                    newHeight = (new Double(newHeight * cale)).intValue();
                    newWidth = widthdist;
                }
            }

            BufferedImage tag = new BufferedImage(widthdist, heightdist, BufferedImage.TRANSLUCENT);

            //计算偏移的坐标
            int x = (widthdist - newWidth) / 2;
            int y = (heightdist - newHeight) / 2;

            //因为要保证留白区域的背景色是白色,所以先用白色画一个满画布的矩型

            // tag.getGraphics().setColor(Color.white);

            //            tag.getGraphics().fillRect(0, 0, widthdist, heightdist);

            Graphics graphics = tag.getGraphics();
            Graphics2D graphics2d = (Graphics2D) graphics;
            //---绘制图片
            graphics2d.drawImage(src.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), x, y, null);

            ImageIO.write(tag, "png", new File(imgdist));

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    /**
     * 生成略缩图，高宽度失调时，将空出的地方进行留白！
     * 
     * @param imgsrc
     *            当前图片的路径
     * @param imgdist
     *            生成图片的路径
     * @param widthdist
     *            略缩图的宽度
     * @param heightdist
     *            略缩图的高度
     */
    public static void reduceImgWhileBk(File srcfile,String imgdist,int widthdist,int heightdist){
        try{
            if (!srcfile.exists()){
                return;
            }
            Image src = ImageIO.read(srcfile);

            int srcWidth = src.getWidth(null);
            int srcHeight = src.getHeight(null);

            int newWidth = widthdist; //欲生成图片的宽

            int newHeight = heightdist; //欲生成图片的高

            float cale = 0f; //比例

            if (srcWidth > srcHeight){ //如果源图的宽大于高

                cale = widthdist * 1f / srcWidth;

                newHeight = (new Double(srcHeight * cale)).intValue();

                if (newHeight > heightdist){
                    cale = heightdist * 1f / newHeight;
                    newWidth = (new Double(newWidth * cale)).intValue();
                    newHeight = heightdist;
                }

            }else{ //如果源图的高大于宽
                cale = heightdist * 1f / srcHeight;

                newWidth = (new Double(srcWidth * cale)).intValue();

                if (newWidth > widthdist){
                    cale = widthdist * 1f / newWidth;
                    newHeight = (new Double(newHeight * cale)).intValue();
                    newWidth = widthdist;
                }
            }

            BufferedImage tag = new BufferedImage(widthdist, heightdist, BufferedImage.TYPE_INT_RGB);

            //计算偏移的坐标
            int x = (widthdist - newWidth) / 2;
            int y = (heightdist - newHeight) / 2;

            //因为要保证留白区域的背景色是白色,所以先用白色画一个满画布的矩型

            tag.getGraphics().setColor(new Color(0, 0, 0));

            tag.getGraphics().fillRect(0, 0, widthdist, heightdist);

            /*
             * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的
             * 优先级比速度高 生成的图片质量比较好 但速度慢
             */
            tag.getGraphics().drawImage(src.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), x, y, null);

            ImageIO.write(tag, "png", new File(imgdist));

            /*
             * FileOutputStream out = new FileOutputStream(imgdist);
             * JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
             * encoder.encode(tag);
             * out.close();
             */

        }catch (IOException ex){
            ex.printStackTrace();
        }

    }

    /**
     * 生成略缩图,如果原图比新的尺寸小，则不生成缩略图
     * false表示目标尺寸太小，不用生成
     * 
     * @param imgsrc
     * @param imgdist
     * @param widthdist
     * @param heightdist
     */
    public static Boolean reduceImgSize(String imgsrc,String imgdist,int widthdist,int heightdist,String reduceType){

        File srcfile = new File(imgsrc);
        if (!srcfile.exists()){
            return false;
        }
        try{
            Image src = ImageIO.read(srcfile);

            //如果原图比新的尺寸小，则不生成缩略图
            if (src.getWidth(null) < widthdist && src.getHeight(null) < heightdist){
                return false;
            }

        }catch (Exception e){
            // TODO Auto-generated catch block
            System.out.println("读取源图失败:" + imgsrc);
        }

        reduceImg(srcfile, imgdist, widthdist, heightdist, reduceType);

        return true;
    }

    /**
     * 生成略缩图
     * 
     * @param imgsrc
     * @param imgdist
     * @param widthdist
     * @param heightdist
     */
    public static void reduceImg(File srcFile,String imgdist,int widthdist,int heightdist,String reduceType){

        if (reduceType.equals("CUT"))
            reduceImgCut(srcFile, imgdist, widthdist, heightdist);
        else if (reduceType.equals("BK"))
            reduceImgWhileBk(srcFile, imgdist, widthdist, heightdist);
        else if (reduceType.equals("PNG"))
            reduceImgWhilePNG(srcFile, imgdist, widthdist, heightdist);
        else if (reduceType.equals("IMMK"))
            reduceImgByImageMagick(srcFile, imgdist, widthdist, heightdist);
        else
            reduceImgMWH(srcFile, imgdist, widthdist, heightdist);

    }

    /**
     * 试用ImgByImageMagick生成高质量略缩图
     * 
     * @param imgsrc
     *            当前图片的路径
     * @param imgdist
     *            生成图片的路径
     * @param widthdist
     *            略缩图的宽度
     * @param heightdist
     *            略缩图的高度
     */
    public static void reduceImgByImageMagick(File srcfile,String imgdist,int widthdist,int heightdist){
        ConvertCmd cmd = new ConvertCmd();
        IMOperation opertion = new IMOperation();

        opertion.addImage(srcfile.getPath());
        // 提升小尺寸缩略图的质量，当然副作用是文件尺寸会变大
        if (widthdist < 300 && heightdist < 300){
            opertion.quality(100.0);
        }
        opertion.resize(widthdist, heightdist);
        opertion.addImage(imgdist);

        Properties pro = ProfileConfigUtil.findPro("config/metainfo.properties");
        cmd.setSearchPath(pro.getProperty("upload.img.immk.path"));

        try{
            cmd.run(opertion);
        }catch (Exception e){
            String errorMsg = String.format("图片缩放失败：%s", imgdist);
            log.error(errorMsg, e);
            throw new BusinessException(errorMsg);
        }
    }

    /**
     * 获取后辍名如.jpg
     * 
     * @param source
     * @return
     */
    public static String getExp(String source){

        int index = source.lastIndexOf(".");
        if (index == -1)
            return "";
        return source.substring(index);
    }

    /**
     * 获取文件名,不包括后辍名
     * 
     * @param source
     * @return
     */
    public static String getFileName(String source){

        int index = source.lastIndexOf("/");
        if (index == -1)
            index = source.lastIndexOf("\\");

        int end = source.lastIndexOf(".");
        if (end == -1)
            end = source.length();
        return source.substring(index + 1, end);
    }

    /**
     * 批量生成略缩图
     * 
     * @param imgSrc
     *            源图片的路径
     * @param dirdist
     *            生成略缩图目录的路径
     * @param whs
     *            略缩图规则，800X600:400X300
     */
    public static void batchReduce(String imgSrc,String dirdist,String whs,String reduceType){
        //文件名
        String fileName = getFileName(imgSrc);
        //扩展名
        String exp = getExp(imgSrc);

        String[] whArray = whs.split(":");

        if (dirdist.lastIndexOf("\\") == -1 && dirdist.lastIndexOf("/") == -1){
            dirdist += "/";
        }

        for (int i = 0; i < whArray.length; i++){
            String[] wh = whArray[i].split("X");
            //生成略缩图
            if (wh.length >= 2)
                reduceImg(
                                new File(imgSrc),
                                dirdist + fileName + "_" + (i + 1) + exp,
                                Integer.parseInt(wh[0]),
                                Integer.parseInt(wh[1]),
                                reduceType);

        }

    }

    /**
     * 生成随机图片名
     * 
     * @RETURN
     */
    public static String getPicName(){
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        sb.append(new Date().getTime()).append(random.nextInt(10000));
        return sb.toString();
    }

    /**
     * 图片地址的相对路径与绝对路径的转换
     * 
     * @param imgUrl
     * @param customBaseUrl
     * @param flag
     * @return
     */
    public static String imageUrlConvert(String imgUrl,String customBaseUrl,Boolean flag){
        if (flag){
            if (StringUtils.isNotBlank(imgUrl) && imgUrl.toLowerCase().startsWith("http://")){
                return imgUrl.replace(customBaseUrl, "");
            }
        }else{
            if (StringUtils.isNotBlank(imgUrl)){
                return customBaseUrl + imgUrl;
            }
        }
        return "";
    }

    public static void main(String[] args) throws Exception{

        reduceImg(new File("d:/577962-815_BL1.png"), "d:/577962-815_BL12.png", 300, 100, "PNG");

    }
}
