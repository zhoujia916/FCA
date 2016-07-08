package puzzle.fca.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.lang.StringBuilder;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="fca_user")
public class FcaUser implements Serializable{ 
	/**
	* Constructor
	*/
	public FcaUser(){
	}
	
	/**
	* Override toString method
	*/
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append("FcaUser{");
        sb.append("userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", password=").append(password);
        sb.append(", birth=").append(birth);
        sb.append(", phone=").append(phone);
        sb.append(", email=").append(email);
        sb.append(", userAvatar=").append(userAvatar);
        sb.append(", openId=").append(openId);
        sb.append(", status=").append(status);
        sb.append(", point=").append(point);
        sb.append(", remark=").append(remark);
        sb.append(", addTime=").append(addTime);
        sb.append(", sortOrder=").append(sortOrder);
        sb.append("}").append(System.getProperty("line.separator"));
        return sb.toString();
	}

	/**
	* Fields
	*/
	@Column(name="user_id", unique=true)
	private Integer userId;
	@Column(name="user_name", nullable=true, length=20)
	private String userName;
	@Column(name="password", nullable=true, length=50)
	private String password;
	@Column(name="birth", nullable=true)
	private Long birth;
	@Column(name="phone", nullable=true, length=20)
	private String phone;
	@Column(name="email", nullable=true, length=20)
	private String email;
	@Column(name="user_avatar", nullable=true, length=255)
	private String userAvatar;
	@Column(name="open_id", nullable=true, length=255)
	private String openId;
	@Column(name="status", nullable=true)
	private Integer status;
	@Column(name="point", nullable=true)
	private Integer point;
	@Column(name="remark", nullable=true, length=500)
	private String remark;
	@Column(name="add_time", nullable=true)
	private Long addTime;
	@Column(name="sort_order", nullable=true)
	private Integer sortOrder;
	
	
	/**
	* Getter and Setter
	*/
	public Integer getUserId(){
		return userId;
	}
	
	public void setUserId(Integer userId){
		this.userId = userId;
	}
		
	public String getUserName(){
		return userName;
	}
	
	public void setUserName(String userName){
		this.userName = userName;
	}
		
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
		
	public Long getBirth(){
		return birth;
	}
	
	public void setBirth(Long birth){
		this.birth = birth;
	}
		
	public String getPhone(){
		return phone;
	}
	
	public void setPhone(String phone){
		this.phone = phone;
	}
		
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
		
	public String getUserAvatar(){
		return userAvatar;
	}
	
	public void setUserAvatar(String userAvatar){
		this.userAvatar = userAvatar;
	}
		
	public String getOpenId(){
		return openId;
	}
	
	public void setOpenId(String openId){
		this.openId = openId;
	}
		
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}
		
	public Integer getPoint(){
		return point;
	}
	
	public void setPoint(Integer point){
		this.point = point;
	}
		
	public String getRemark(){
		return remark;
	}
	
	public void setRemark(String remark){
		this.remark = remark;
	}
		
	public Long getAddTime(){
		return addTime;
	}
	
	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}
		
	public Integer getSortOrder(){
		return sortOrder;
	}
	
	public void setSortOrder(Integer sortOrder){
		this.sortOrder = sortOrder;
	}

    private String birthString;

    public String getBirthString() {
        return birthString;
    }

    public void setBirthString(String birthString) {
        this.birthString = birthString;
    }
}
