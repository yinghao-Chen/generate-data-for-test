<template>
  <el-row>
    <el-col :span="10">
      <el-input
        v-model="textarea1"
        :autosize="{ minRows: 40, maxRows: 40 }"
        type="textarea"
        placeholder="Please input"
      />
    </el-col>

    <el-col :span="4">
      <el-button type="success" @click="generate">生成</el-button>
      
      <div class="pre-cls-info">
        输入示例：
        <pre>{{ textInfo }}</pre>
      </div>
    </el-col>

    <el-col :span="10">
      <pre class="pre-cls">{{ textarea2 }}</pre>
    </el-col>
  </el-row>
</template>

<script>
import { ref } from 'vue'

export default {
  setup() {
      return {
        textarea1: ref(''),
        textarea2: ref(''),
        textInfo: ref('')
      };
  },
  mounted() {
    const info = '{"sfzh":"@sfzh","string|1-10":"★","string2|3":"★★","number|+1":202,"number2|1-100.1-10":1,"boolean|1":true,"regexp|reg":"/[a-z][A-Z][0-9]/","absolutePath":"@/string @/user/name","user":{"name":"demo"},"object|2":{"310000":"上海市","320000":"江苏省"},"array|1":["AMD"],"string3|1-2":"@str","integer":"@int(10, 30)","float":"@float(60, 100, 2, 2)","boolean":"@boolean","date":"@date(yyyy-MM-dd)","datetime":"@datetime","now":"@now","url":"@url","email":"@email","city":"@city","province":"@province","country":"@country","country":"@country_en","id":"@id","id":"@uuid"}'
    this.textInfo = JSON.parse(info)
  },
  methods: {
    async generate() {
      if(this.textarea1) {
        const res = await this.$api.generate(this.textarea1)
        if(res.code == 200) {
          this.textarea2 = res.data
        }
      }
    }
  }
}
</script>
<style>
.pre-cls{
  width: 100%;
  height: 98%;
  border: 1px solid #dcdfe6;
}
.pre-cls-info{
  overflow: auto;
  width: 100%;
  height: 93%;
  margin-top: 23px;
  border: 1px solid #0080004f;
}
</style>