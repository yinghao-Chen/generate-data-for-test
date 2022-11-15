package cn.cyh.generatedata.controller;

import cn.cyh.generatedata.api.common.Result;
import cn.cyh.generatedata.service.RandomData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author cyh
 * @date 2022/11/14
 */
@RestController("gen")
public class GenerateController {

    private final RandomData randomData;

    @Autowired
    GenerateController(RandomData randomData) {
        this.randomData = randomData;
    }

    /**
     * 格式定义如下:
     * {
     *   "sfzh": "@sfzh",
     *
     *   "string|1-10": "★", // ★
     *   "string2|3": "★★", // ★★★★★★
     *   "number|+1": 202,      //202
     *   "number2|1-100.1-10": 1,   //71.73566
     *   "boolean|1": true,         //false
     *   "regexp|reg": /[a-z][A-Z][0-9]/,   // qS8
     *   "absolutePath": "@/string @/user/name",    //★ demo
     *   "user": {
     *     "name": "demo"
     *   },
     *   "object|2": {
     *     "310000": "上海市",
     *     "320000": "江苏省"
     *   }, // {"310000": "上海市","320000": "江苏省"}
     *   "array|1": [ "AMD" ]   //AMD
     *
     *   "string|1-2": "@string",   //&b(V
     *   "integer": "@integer(10, 30)", //29
     *   "float": "@float(60, 100, 2, 2)",  //65.93
     *   "boolean": "@boolean",     //true
     *   "date": "@date(yyyy-MM-dd)",   //2013-02-05
     *   "datetime": "@datetime",   //1983-09-13 16:25:29
     *   "now": "@now",     //2017-08-12 01:16:03
     *   "url": "@url",     //cid://vqdwk.nc/iqffqrjzqa
     *   "email": "@email", //u.ianef@hcmc.bv
     *   "region": "@region",   //华南
     *   "city": "@city",       //通化市
     *   "province": "@province",   //陕西省
     *   "county": "@county",   //嵊州市
     *   "upper": "@upper(@title)", //DGWVCCRR TLGZN XSFVHZPF TUJ
     *   "guid": "@guid",   //c09c7F2b-0AEF-B2E8-74ba-E1efC0FecEeA
     *   "id": "@id",       //650000201405028485
     *   "image": "@image(200x200)",    //http://dummyimage.com/200x200
     *   "title": "@title",     //Orjac Kwovfiq Axtwjlop Xoggxbxbw
     *   "cparagraph": "@cparagraph",   //他明林决每别精与界受部因第方。习压直型示多性子主求求际后世
     *   "csentence": "@csentence",     //命己结最方心人车据称温增划眼难。
     *   "range": "@range(2, 10)"   //[2, 3, 4, 5, 6, 7, 8, 9]
     *
     *   "sm": "@nullable"
     * }
     * @param map 自定义格式: 支持map
     * @return 生成的随机数据
     */
    @PostMapping("/data")
    public Result randomData(@RequestBody Map<String, Object> map) {
        return Result.data(randomData.generate(map));
    }

}
