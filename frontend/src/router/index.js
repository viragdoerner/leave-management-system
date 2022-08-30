import Vue from 'vue'
import VueRouter from 'vue-router'
import UserDashboard from '../views/UserDashboard.vue'
import AdminDashboard from '../views/AdminDashboard.vue'
import UserManagementDashboard from '../views/UserManagementDashboard.vue'
import Login from '../views/Login.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'UserDashboard',
    component: UserDashboard
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: AdminDashboard
  },
  {
    path: '/admin-users',
    name: 'UserManagementDashboard',
    component: UserManagementDashboard
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '*',
    redirect: "/"
  }
]

const router = new VueRouter({
  routes
})
router.beforeEach((to, from, next) => {
  const adminPages = ['/admin', '/admin-users'];
  const userPages = ['/'];
  const adminAuthRequired = adminPages.includes(to.path);
  const authRequired = userPages.includes(to.path);

  const user = JSON.parse(localStorage.getItem('user'));

  var loggedIn = false;
  var isAdmin = false;
  if (!!user) {
    loggedIn = true;
    if (user.authorities.length > 1) {
      isAdmin = true;
    }
  }
  if (to.path === "/" && loggedIn && isAdmin) {
    next('/admin');
  }

  if (to.path === "/login" && loggedIn) {
    isAdmin ? next('/admin'): next("/");
  }


  if ((authRequired && !loggedIn) || (adminAuthRequired && !isAdmin)) {
    next('/login');
  } else {
    next();
  }
});

export default router
