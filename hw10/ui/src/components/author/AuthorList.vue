<template>
  <h2 class="mb-5 text-center">Доступные авторы</h2>

  <div class="container col-6">
    <table class="table">
      <thead>
      <tr>
        <th scope="col">Id</th>
        <th scope="col">Имя</th>
      </tr>
      </thead>

      <tbody v-for="(author) in authors" :key="author.id">
      <tr>
        <td>{{ author.id }}</td>
        <td>{{ author.fullName }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>

import AuthorDataService from '../../services/AuthorDataService';

export default {
  name: 'AuthorList',
  data(){
    return {
      authors: []
    }
  },
  methods: {
    getAuthors(){
      AuthorDataService.getAll().then((response) => {
        this.authors = response.data;
      });
    }
  },
  created() {
    this.getAuthors();
  }
}

</script>