"use strict";
import request from "@/util/request";

export const generate = (data) => {
  return request("/gen/data", {
    method: "POST",
    data
  });
}
 