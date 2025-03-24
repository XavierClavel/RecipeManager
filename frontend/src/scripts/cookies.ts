

export {
COOKIE_LOCALE,
getCookie,
setCookie,
deleteCookie,
}

const COOKIE_LOCALE = "locale"

function getCookie(key) {
  return $cookies.get(key)
}

function setCookie(key, value) {
  $cookies.set(key, value)
}

function deleteCookie(key) {
  $cookies.remove(key)
}
