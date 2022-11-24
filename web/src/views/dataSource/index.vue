<template>
  <el-row>
    <el-col :span="24">
      <el-button type="primary" @click="back">返回</el-button>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="24">
      <div class="sel-form-label">选择一个数据源</div>
      <el-select v-model="type" placeholder="Please select a datasource" class="sel-form-split" @change="cahngeType">
        <el-option label="mysql" value="mysql" />
        <el-option label="oracle" value="oracle" />
        <el-option label="elastic search" value="es" />
      </el-select>
    
      <!--mysql-->
      <el-form :model="form" label-width="120px" v-if="type == 'mysql'">
        <el-form-item label="url">
          <el-input v-model="meta.url" placeholder="eg: jdbc:mysql://192.168.0.86:3306/test" />
        </el-form-item>
        <el-form-item label="username">
          <el-input v-model="meta.username" placeholder="Please input username" />
        </el-form-item>
        <el-form-item label="password">
          <el-input v-model="meta.password" type="password" placeholder="Please input password" />
        </el-form-item>
        <el-form-item label="数据表">
          <el-input v-model="meta.table" placeholder="Please input table" />
        </el-form-item>
        <el-form-item label="数据量">
          <el-input-number
            v-model="meta.count"
            class="mx-4"
            :min="1"
            :max="1000000"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item>
        <el-alert title="tips：数据源中不存在的字段会忽略。" type="info" show-icon />
        <el-input
          v-model="ruleData"
          :autosize="{ minRows: 20, maxRows: 20 }"
          type="textarea"
          :placeholder="ruleText"
        />
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="test">test connection</el-button>
          <el-button type="primary" @click="onSubmit">Generate</el-button>
        </el-form-item>
      </el-form>

      <!--oracle-->
      <el-form :model="form" label-width="120px" v-if="type == 'oracle'">
        <el-form-item label="url">
          <el-input v-model="meta.url" placeholder="eg: jdbc:oracle:thin:@127.0.0.1:1521:orcl" />
        </el-form-item>
        <el-form-item label="username">
          <el-input v-model="meta.username" placeholder="Please input username" />
        </el-form-item>
        <el-form-item label="password">
          <el-input v-model="meta.password" type="password" placeholder="Please input password" />
        </el-form-item>
        <el-form-item label="数据库表">
          <el-input v-model="meta.table" placeholder="Please input table,like this: db:test" />
        </el-form-item>
        <el-form-item label="数据量">
          <el-input-number
            v-model="meta.count"
            class="mx-4"
            :min="1"
            :max="1000000"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item>
        <el-alert title="tips：数据源中不存在的字段会忽略。" type="info" show-icon />
        <el-input
          v-model="ruleData"
          :autosize="{ minRows: 20, maxRows: 20 }"
          type="textarea"
          :placeholder="ruleText"
        />
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="test">test connection</el-button>
          <el-button type="primary" @click="onSubmit">Generate</el-button>
        </el-form-item>
      </el-form>

      <!--es-->
      <el-form :model="form" label-width="120px" v-if="type == 'es'">
        <el-form-item label="url">
          <el-input v-model="meta.url" placeholder="eg: 127.0.0.1" />
        </el-form-item>
        <el-form-item label="索引">
          <el-input v-model="meta.table" placeholder="Please input index" />
        </el-form-item>
        <el-form-item label="类型">
          <el-input v-model="meta.type" placeholder="Please input type" />
        </el-form-item>
        <el-form-item label="数据量">
          <el-input-number
            v-model="meta.count"
            class="mx-4"
            :min="1"
            :max="1000000"
            controls-position="right"
          />
        </el-form-item>
        <el-form-item>
        <el-alert title="tips：数据库中不存在的字段会忽略。" type="info" show-icon />
        <el-input
          v-model="ruleData"
          :autosize="{ minRows: 20, maxRows: 20 }"
          type="textarea"
          :placeholder="ruleText"
        />
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="test">test connection</el-button>
          <el-button type="primary" @click="onSubmit">Generate</el-button>
        </el-form-item>
      </el-form>
      
    </el-col>
  </el-row>
</template>

<script>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

export default {
  setup() {
      return {
        type: ref('mysql'),
        meta: ref({
          url: '',
          username: '',
          password: '',
          table: '',
          type: '',
          count: 100
        }),
        ruleData: ref(''),
        ruleText: ref('error')
      };
  },
  mounted() {
    const text = '{"sfzh":"@sfzh","string|1-10":"★","string2|3":"★★","number|+1":"202","number2|1-10.1-4":1,"boolean|1":true,"regexp|reg":"[a-z][A-Z][0-9]\\d","absolutePath":"@:string3  @:user:name","user":{"name":"demo"},"object|1":{"310000":"上海市","320000":"江苏省"},"array|1":["AMD","CDM"],"string3|1-2":"@str","integer":"@int(10, 30)","float":"@float(60, 100, 2, 2)","boolean":"@boolean","date":"@date(yyyy-MM-dd)","datetime":"@datetime","now":"@now","url":"@url","email":"@email","city":"@city","province":"@province","country":"@country","country_en":"@country_en","id":"@id","uuid":"@uuid"}'
    this.ruleText = text.replace(/,\"|{|},|],|}|:{|:\[/g, (match) => {
      if(/,\"/.test(match)) {
        return ',\n    \"'
      } else if(/{/.test(match)) {
        return match + '\n    '
      } else if(/:{/.test(match)) {
        return match + '\n        '
      } else if(/},|],/.test(match)) {
        return  '\n    ' + match + '\n    '
      } else if(/}/.test(match)) {
        return '\n' + match
      } else if(/:\[/.test(match)) {
        return match + '\n    '
      }
      return match
    })

    this.cahngeType()
  },
  methods: {
    back() {
      this.$router.go(-1)
    },
    cahngeType() {
      const _data = localStorage.getItem(`CACHE_${this.type}_DATA`)
      if(_data) {
        const data = JSON.parse(_data)
        if(data.meta){
          this.meta = data.meta
        }
        this.ruleData = _data.ruleData ? JSON.stringify(_data.ruleData) : ''
      } else {
        this.meta = {
            url: '',
            username: '',
            password: '',
            table: '',
            type: '',
            count: 100
          }
        this.ruleData = ''
      }
    },
    async test() {
      const { meta,type } = this
      let res = {};
      if(type == 'mysql') {
        const _data = Object.assign({
          url: '',
          username: '',
          password: ''
        }, meta)
        res = await this.$api.toDataTest(_data)
      } else if(type == 'oracle') {
        
      } else if(type == 'es') {
        const _data = Object.assign({
          url: ''
        }, meta)
        res = await this.$api.toDataTestEs(_data)
      }
      if(res.code == 200) {
        ElMessage({
          showClose: true,
          message: '连接成功',
          type: 'success',
        })
      } else {
        ElMessage({
          showClose: true,
          message: '连接失败',
          type: 'error',
        })
      }
    },
    async onSubmit() {
      const { meta, ruleData, type } = this

      const _data = {
        meta: Object.assign({
          url: '',
          username: '',
          password: '',
          table: '',
          type: '',
          count: 1
        }, meta),
        ruleData: ruleData ? JSON.parse(ruleData) : ''
      }
      localStorage.setItem(`CACHE_${type}_DATA`, JSON.stringify(_data))

      if(type == 'mysql') {
        if(!meta.url || !meta.username || !meta.password || !meta.table) {
          ElMessage({
            showClose: true,
            message: '请输入数据源信息',
            type: 'warning',
          })
          return
        }
      } else if(type == 'oracle') {

      } else if(type == 'es') {
        if(!meta.url || !meta.table || !meta.type) {
          ElMessage({
            showClose: true,
            message: '请输入数据源信息',
            type: 'warning',
          })
          return
        }
      }

      if(!ruleData) {
        ElMessage({
          showClose: true,
          message: '请输入数据生成规则',
          type: 'warning',
        })
        return
      }

      let res = {}
      if(type == 'mysql') {
        res = await this.$api.toDatasource(_data)
      } else if(type == 'oracle') {

      } else if(type == 'es') {
        res = await this.$api.toDatasourceEs(_data)
      }
      if(res.code == 200) {
        ElMessage({
          showClose: true,
          message: res.data || '操作成功',
          type: 'success',
        })
      } else {
        ElMessage({
          showClose: true,
          message: res.msg || '操作失败',
          type: 'error',
        })
      }
    }
  }
}
</script>
<style>
.sel-form-label{
  width: 120px;
  display: inline-block;
  height: 32px;
  margin-bottom: 30px;
  position: relative;
  vertical-align: middle;
  line-height: 32px;
}
.sel-form-split{
  margin-bottom: 30px;
}
</style>