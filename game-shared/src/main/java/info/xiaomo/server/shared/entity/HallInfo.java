/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.xiaomo.server.shared.entity;

import lombok.Data;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * 全局大厅服务器信息
 */
@Entity(value = "hall_info", noClassnameStored = true)
@Data
public class HallInfo {
    @Id
    private int id = 1;
    /**
     * 用户ID编号
     */
    private long userIdNum;
    /**
     * 角色ID编号
     */
    private long roleIdNum;


}
