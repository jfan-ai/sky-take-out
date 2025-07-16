package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public void update(SetmealDTO setmealDTO) {
        //获得setmeal
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        //获得套餐菜品关系
        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        //删除原来的套餐菜品关系
        setmealMapper.deleteSetmealDishBySetmealId(setmealDTO.getId());
        list.forEach(setmealDish -> setmealDish.setSetmealId(setmealDTO.getId()));
        setmealMapper.insertSetmealDish(list);
    }

    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        List<SetmealDish> list = setmealDTO.getSetmealDishes();
        setmealMapper.insert(setmeal);
        /**
         * 为list中的每个setmealdish设置setmealId
         */
        list.forEach(setmealDish -> setmealDish.setSetmealId(setmeal.getId()));
        setmealMapper.insertSetmealDish(list);
    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        /**
         * 套餐分页查询
         */
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> list = setmealMapper.listSetmealDishesBySetmealId(id);
        return SetmealVO.builder()
                .id(setmeal.getId())
                .categoryId(setmeal.getCategoryId())
                .name(setmeal.getName())
                .price(setmeal.getPrice())
                .status(setmeal.getStatus())
                .description(setmeal.getDescription())
                .image(setmeal.getImage())
                .updateTime(setmeal.getUpdateTime())
                .categoryName(categoryMapper.selectNameById(setmeal.getCategoryId()))
                .setmealDishes(list).build();
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId( id);
        setmeal.setStatus(status);
        setmealMapper.update(setmeal);
    }

    @Override
    public void delete(List<Long> ids) {
        setmealMapper.delete(ids);
    }
}
