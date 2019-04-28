package cn.harmonycloud.datacenter.mapper;

import cn.harmonycloud.datacenter.entity.mysql.ForecastCell;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Select("SELECT * FROM forecastcell WHERE cell_id=#{cellId} AND type=#{type} AND forecasting_index=#{forecastingIndex}")
    public ForecastCell getOneForecastCellByIndex(String cellId, int type, String forecastingIndex);

    @Update("UPDATE forecastcell SET forecasting_model=#{forecastingModel},model_params=#{modelParams} " +
            "WHERE cell_id=#{cellId} AND type=#{type} AND forecasting_index=#{forecastingIndex}")
    public int updateForecastingModel(String cellId, int type, String forecastingIndex, String forecastingModel,
                                     String modelParams);

    @Insert("INSERT INTO forecastcell (cell_id,type,forecasting_index,forecasting_model,model_params) " +
            "VALUES (#{cellId},#{type},#{forecastingIndex},#{forecastingModel},#{modelParams})")
    public int insertForecastingModel(String cellId, int type, String forecastingIndex, String forecastingModel,
                                       String modelParams);

    @Update("UPDATE forecastcell SET forecasting_end_time=#{forecastingEndTime} " +
            "WHERE cell_id=#{cellId} AND type=#{type} AND forecasting_index=#{forecastingIndex}")
    public int updateForecastingEndTime(String cellId, int type, String forecastingIndex, String forecastingEndTime);

    @Insert("INSERT INTO forecastcell (cell_id,type,forecasting_index,forecasting_end_time) " +
            "VALUES (#{cellId},#{type},#{forecastingIndex},#{forecastingEndTime})")
    public int insertForecastingEndTime(String cellId, int type, String forecastingIndex, String forecastingEndTime);
}
