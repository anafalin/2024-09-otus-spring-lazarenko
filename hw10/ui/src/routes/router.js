import { createRouter, createWebHistory } from 'vue-router';

const routes = [
    {
        path: '/',
        name: 'start',
        component: () => import('../components/StartPage.vue'),
    },
    {
        path: '/books',
        name: 'books',
        component: () => import('../components/book/BooksList.vue')
    },
    {
        path: '/books/:id',
        name: 'edit-book',
        component: () => import('../components/book/EditBook.vue')
    },
    {
        path: '/books/create',
        name: 'create-book',
        component: () => import('../components/book/CreateBook.vue')
    },
    {
        path: '/genres',
        name: 'genres',
        component: () => import('../components/genre/GenreList.vue')
    },
    {
        path: '/authors',
        name: 'authors',
        component: () => import('../components/author/AuthorList.vue')
    },
    {
        path: '/comments',
        name: 'comments',
        component: () => import('../components/comment/BookCommentsList.vue')
    },
    {
        path: '/comments/:id',
        name: 'edit-comment',
        component: () => import('../components/comment/EditComment.vue')
    },
    {
        path: '/comments/create/:id',
        name: 'create-comment',
        component: () => import('../components/comment/CreateComment.vue')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
