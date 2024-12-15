<template>
  <div class="container">

    <div id="app">
      <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
          <router-link to="/" class="navbar-brand">Library</router-link>

          <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                  aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>

          <div class="collapse navbar-collapse" id="navbarNav">
            <div class="navbar-nav">
              <router-link to="/books" class="nav-link">Книги</router-link>
              <router-link to="/authors" class="nav-link">Авторы</router-link>
              <router-link to="/genres" class="nav-link">Жанры</router-link>
              <router-link to="/books/create" class="nav-link">Создать книгу</router-link>
            </div>
          </div>
        </div>
      </nav>

      <div class="container my-4">
        <router-view/>
      </div>

    </div>
  </div>
</template>