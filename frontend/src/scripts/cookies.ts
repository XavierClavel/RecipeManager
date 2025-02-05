

export {
COOKIE_LOCALE,
getCookie,
setCookie,
}

const COOKIE_LOCALE = "locale"

function getCookie(key) {
  return $cookies.get(key)
}

function setCookie(key, value) {
  $cookies.set(key, value)
}
