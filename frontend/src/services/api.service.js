import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/';
//const API_URL = 'https://leave-management-backend.herokuapp.com/';


class ApiService {
    API_URL = API_URL

    GET(url) {
        return axios.get(API_URL + url, { headers: authHeader() });
    }
    POST(url, param) {
        return axios.post(API_URL + url, param, { headers: authHeader() });
    }
    PUT(url, param) {
        return axios.put(API_URL + url, param, { headers: authHeader() });
    }
    DELETE(url) {
        return axios.delete(API_URL + url, { headers: authHeader() });
    }
}
export default new ApiService();