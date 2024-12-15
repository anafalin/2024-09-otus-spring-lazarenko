<template>
  <h2 class="mb-5 text-center">Доступные жанры</h2>

  <div class="container col-4">
    <table class="table">
      <thead>
      <tr>
        <th scope="col">Id</th>
        <th scope="col">Название</th>
      </tr>
      </thead>

      <tbody v-for="(genre) in genres" :key="genre.id">
      <tr>
        <td>{{ genre.id }}</td>
        <td>{{ genre.name }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>

import GenreDataService from '../../services/GenreDataService';

export default {
  name: 'GenreList',
  data(){
    return {
      genres: []
    }
  },
  methods: {
    getGenres(){
      GenreDataService.getAll().then((response) => {
        this.genres = response.data;
      });
    }
  },
  created() {
    this.getGenres();
  }
}

</script>