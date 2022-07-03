package com.my.demogite.entity.dto;


import com.my.demogite.entity.Setmeal;
import com.my.demogite.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
