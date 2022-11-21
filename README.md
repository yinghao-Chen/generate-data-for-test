## 为开发造测试数据而生。
#### like this:
```
POST localhost:8080/gen/data
-d
{
    "sfzh": "@sfzh",
    "string|1-10": "★",
    "string2|3": "★★",
    "number|+1": "202",
    "number2|1-10.1-4": 1,
    "boolean|1": true,
    "regexp|reg": "[a-z][A-Z][0-9]\\d",
    "absolutePath": "@:string3  @:user:name",
    "user": {
        "name": "demo"
    },
    "object|1": {
        "310000": "上海市",
        "320000": "江苏省"
    },
    "array|1": [
        "AMD","CDM"
    ],
    "string3|1-2": "@str",
    "integer": "@int(10, 30)",
    "float": "@float(60, 100, 2, 2)",
    "boolean": "@boolean",
    "date": "@date(yyyy-MM-dd)",
    "datetime": "@datetime",
    "now": "@now",
    "url": "@url",
    "email": "@email",
    "city": "@city",
    "province": "@province",
    "country": "@country",
    "country_en": "@country_en",
    "id": "@id",
    "uuid": "@uuid"
}
```

#### can create data: 
```
{
	"code": 200,
	"success": true,
	"data": {
		"sfzh": "712791201702080558",
		"string": "★★★★★★★★★★",
		"string2": "★★★★★★",
		"number": "202202202",
		"number2": "8.2",
		"boolean": true,
		"regexp": "tD29",
		"absolutePath": "gkZImJ7QHn  demo",
		"user": {
			"name": "demo"
		},
		"object": {
			"320000": "江苏省"
		},
		"array": [
			"AMD"
		],
		"string3": "gkZImJ7QHn",
		"integer": "22",
		"float": "98.57",
		"date": "1990-03-30",
		"datetime": "1988-01-07 16:58:47",
		"now": "2022-11-21 11:14:17",
		"url": "http://192.168.36.8:5069/4v/SW/",
		"email": "Rly@4PR.com",
		"city": "唐山市",
		"province": "山东省",
		"country": "巴西",
		"country_en": "PAPUA NEW GUINEA",
		"id": "271022694437359616",
		"uuid": "948ace45fc824dd1801c328c0bda265c"
	},
	"msg": "操作成功"
}
```

#### 待实现功能
- 增加页面输入参数，测试得到结果
- 增加生成结果联动（依赖关系字段）
- 添加各种数据源，配置生成数据量，将生成的数据实例化，方便开发和测试
- 优化生成性能
- ...