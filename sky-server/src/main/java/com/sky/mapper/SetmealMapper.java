package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);
    @Select("select count(id) from setmeal_dish where dish_id = #{dishId}")
    Integer countByDishId(Long id);
    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteSetmealDishBySetmealId(Long setmealId);

    void insertSetmealDish(List<SetmealDish> list);
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    List<SetmealDish> listSetmealDishesBySetmealId(Long id);

    void delete(List<Long> ids);

    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    List<Setmeal> list(Setmeal setmeal);
}
