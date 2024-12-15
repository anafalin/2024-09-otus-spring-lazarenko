import axios from "axios";
import BASE_URL from "@/config";

const RESOURCE_NAME = `${BASE_URL}/comments`;

class  CommentDataService{
  getAllByBookId(bookId) {
    return axios.get(`${RESOURCE_NAME}?bookId=${bookId}`);
  }

  getById(id) {
    return axios.get(`${RESOURCE_NAME}/${id}`);
  }

  deleteById(id) {
    return axios.delete(`${RESOURCE_NAME}/${id}`);
  }

  updateById(id, comment) {
    return axios.put(`${RESOURCE_NAME}/${id}`, comment);
  }

  create(comment) {
    return axios.post(RESOURCE_NAME, comment);
  }

}

export default new CommentDataService();