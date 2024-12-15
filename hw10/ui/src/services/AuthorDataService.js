import axios from "axios";
import BASE_URL from "@/config";

const RESOURCE_NAME = `${BASE_URL}/authors`;

class AuthorDataService {
  getAll() {
    return axios.get(RESOURCE_NAME);
  }
}

export default new AuthorDataService;