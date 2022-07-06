package com.my.demogite.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.demogite.entity.AddressBook;
import com.my.demogite.mapper.AddressBookMapper;
import com.my.demogite.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
