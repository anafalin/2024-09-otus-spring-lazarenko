import axios from "axios";
import BASE_URL from "@/config";

const RESOURCE_NAME = `${BASE_URL}/books`;

class BookDataService {
  getAll() {
    return axios.get(RESOURCE_NAME);
  }

  getById(id) {
    return axios.get(`${RESOURCE_NAME}/${id}`);
  }

  create(book) {
    return axios.post(RESOURCE_NAME, book);
  }

  updateById(id, book) {
    return axios.put(`${RESOURCE_NAME}/${id}`, book);
  }

  deleteById(id) {
    return axios.delete(`${RESOURCE_NAME}/${id}`);
  }

}

export default new BookDataService;