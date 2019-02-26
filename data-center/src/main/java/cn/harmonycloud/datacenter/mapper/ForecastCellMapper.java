package cn.harmonycloud.datacenter.mapper;

import cn.harmonycloud.datacenter.entity.mysql.ForecastCell;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *@Author: shaodilong
 *@Description:
 *@Date: Created in 2019/1/24 16:22
 *@Modify By:
 */
@Mapper
public interface ForecastCellMapper {

    @Select("SELECT * FROM forecastcell")
    public List<ForecastCell> getAllForecastCells();
}
