package generator.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
* 
* @TableName t_living_room
*/
public class TLivingRoom implements Serializable {

    /**
    * 
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    private Integer id;
    /**
    * 主播id
    */
    @ApiModelProperty("主播id")
    private Long anchorId;
    /**
    * 直播间类型（1普通直播间，2pk直播间）
    */
    @NotNull(message="[直播间类型（1普通直播间，2pk直播间）]不能为空")
    @ApiModelProperty("直播间类型（1普通直播间，2pk直播间）")
    private Integer type;
    /**
    * 状态（0无效1有效）
    */
    @NotNull(message="[状态（0无效1有效）]不能为空")
    @ApiModelProperty("状态（0无效1有效）")
    private Integer status;
    /**
    * 直播间名称
    */
    @Size(max= 60,message="编码长度不能超过60")
    @ApiModelProperty("直播间名称")
    @Length(max= 60,message="编码长度不能超过60")
    private String roomName;
    /**
    * 直播间封面
    */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("直播间封面")
    @Length(max= 255,message="编码长度不能超过255")
    private String covertImg;
    /**
    * 观看数量
    */
    @ApiModelProperty("观看数量")
    private Integer watchNum;
    /**
    * 点赞数量
    */
    @ApiModelProperty("点赞数量")
    private Integer goodNum;
    /**
    * 开播时间
    */
    @ApiModelProperty("开播时间")
    private Date startTime;
    /**
    * 
    */
    @ApiModelProperty("")
    private Date updateTime;

    /**
    * 
    */
    private void setId(Integer id){
    this.id = id;
    }

    /**
    * 主播id
    */
    private void setAnchorId(Long anchorId){
    this.anchorId = anchorId;
    }

    /**
    * 直播间类型（1普通直播间，2pk直播间）
    */
    private void setType(Integer type){
    this.type = type;
    }

    /**
    * 状态（0无效1有效）
    */
    private void setStatus(Integer status){
    this.status = status;
    }

    /**
    * 直播间名称
    */
    private void setRoomName(String roomName){
    this.roomName = roomName;
    }

    /**
    * 直播间封面
    */
    private void setCovertImg(String covertImg){
    this.covertImg = covertImg;
    }

    /**
    * 观看数量
    */
    private void setWatchNum(Integer watchNum){
    this.watchNum = watchNum;
    }

    /**
    * 点赞数量
    */
    private void setGoodNum(Integer goodNum){
    this.goodNum = goodNum;
    }

    /**
    * 开播时间
    */
    private void setStartTime(Date startTime){
    this.startTime = startTime;
    }

    /**
    * 
    */
    private void setUpdateTime(Date updateTime){
    this.updateTime = updateTime;
    }


    /**
    * 
    */
    private Integer getId(){
    return this.id;
    }

    /**
    * 主播id
    */
    private Long getAnchorId(){
    return this.anchorId;
    }

    /**
    * 直播间类型（1普通直播间，2pk直播间）
    */
    private Integer getType(){
    return this.type;
    }

    /**
    * 状态（0无效1有效）
    */
    private Integer getStatus(){
    return this.status;
    }

    /**
    * 直播间名称
    */
    private String getRoomName(){
    return this.roomName;
    }

    /**
    * 直播间封面
    */
    private String getCovertImg(){
    return this.covertImg;
    }

    /**
    * 观看数量
    */
    private Integer getWatchNum(){
    return this.watchNum;
    }

    /**
    * 点赞数量
    */
    private Integer getGoodNum(){
    return this.goodNum;
    }

    /**
    * 开播时间
    */
    private Date getStartTime(){
    return this.startTime;
    }

    /**
    * 
    */
    private Date getUpdateTime(){
    return this.updateTime;
    }

}
