import axios from "axios";


// 创建 axios 实例
const service = axios.create({
    timeout: 60000, // 请求超时时间
    baseURL: baseURL,
    // withCredentials: true
});

const err = (error) => {
    const { response } = error;
    if (response && response.status) {
        const { status } = response;
        window.$notification.error({
            content: `请求错误 ${status}`,
            meta: "接口错误,请联系管理员",
            duration: 2500,
        });
    } else if (!response) {
        window.$notification.error({
            content: "网络异常",
            meta: "请检查您的网络",
            duration: 2500,
        });
    }
    return response;
};

service.interceptors.request.use((config) => {
    config.headers['Content-Type'] = 'application/json;charset=UTF-8';
    return config;
}, err);

service.interceptors.response.use((response) => {
    if (
        response.data.code !== 0 &&
        response.data.code !== 200
    ) {
        window.$message.warning(response.data.message || response.data.msg);
    }
    return response.data;
}, err);

export default service;
