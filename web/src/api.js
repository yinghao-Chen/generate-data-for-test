"use strict";
import request from "@/util/request";

// 生成一条数据
export const generate = (data) => {
  return request("/gen/data", {
    method: "POST",
    data
  });
}
// 测试数据源-mysql
export const toDataTest = (data) => {
  return request("/toData/test", {
    method: "POST",
    data
  });
}
// 生成数据到数据源-mysql
export const toDatasource = (data) => {
  return request("/toData/save", {
    method: "POST",
    data
  });
}
// 测试数据源-es
export const toDataTestEs = (data) => {
  return request("/toData/es/test", {
    method: "POST",
    data
  });
}
// 生成数据到数据源-es
export const toDatasourceEs = (data) => {
  return request("/toData/es/save", {
    method: "POST",
    data
  });
}
 