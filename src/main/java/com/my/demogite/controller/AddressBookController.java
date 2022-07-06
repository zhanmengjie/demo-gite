package com.my.demogite.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.my.demogite.common.BaseContext;
import com.my.demogite.common.R;
import com.my.demogite.entity.AddressBook;
import com.my.demogite.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook
     * @return
     */
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("当前登录用户的id : {}",addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }
    /**
     * 设置默认地址
     */
    @PutMapping("/default")
    public R<AddressBook> update(@RequestBody AddressBook addressBook) {
        log.info("address :{}" ,addressBook);
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault,0);
        addressBookService.update(updateWrapper);
        //设置默认的地址
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }
    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable("id") Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        }else {
            return R.error("没有查到该地址");
        }
    }
    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    public R<AddressBook> getDefaultAddressBook(){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,BaseContext.getCurrentId())
                .eq(AddressBook::getIsDefault,1);
        AddressBook defaultAddressBook = addressBookService.getOne(queryWrapper);
        if (defaultAddressBook != null) {
            return R.success(defaultAddressBook);
        }else {
            return R.error("没有查询到默认地址!");
        }
    }
    /**
     * 查询当前登录用户的全部地址信息
     */
    @GetMapping("/list")
    public R<List<AddressBook>> getListAddressBook(AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId() != null,AddressBook::getUserId,addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        return R.success(addressBookService.list(queryWrapper));
    }
    /**
     * 修改保存地址
     */
    @PutMapping
    public R<AddressBook> updateAndSaveAddress(@RequestBody AddressBook addressBook){
        log.info("地址id为: {}" ,addressBook.toString());
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }
    /**
     * 删除收获地址
     */
    @DeleteMapping
    public R<String> deleteAddress(Long ids){
        log.info("获取到的地址id是: {}",ids.toString());
        addressBookService.removeById(ids);
        return R.success("地址删除成功");
    }

}
