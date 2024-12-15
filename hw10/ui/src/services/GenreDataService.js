import axios from "axios";
import BASE_URL from "@/config";

const RESOURCE_NAME = `${BASE_URL}/genres`;

class GenreDataService{
  getAll() {
    return axios.get(RESOURCE_NAME);
  }
}

export default new GenreDataService();