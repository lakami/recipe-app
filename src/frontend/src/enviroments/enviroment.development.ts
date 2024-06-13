const baseUrl: string = '/api/v1';
const base = (path: string) => baseUrl + path;

export const environment = {
  login: base('/login'),
  logout: base('/logout'),
  account: base('/account'),
  register: base('/register'),
  activate: base('/activate?token='),
  recipe: base('/recipe'),
  dish: base('/dish'),
  diet: base('/diet'),
  tag: base('/tag'),
  favourites: base('/favourites'),
  promoted: base('/promoted'),
  comment: base('/comment'),
  user: base('/user'),
  images: base('/images'),
}
