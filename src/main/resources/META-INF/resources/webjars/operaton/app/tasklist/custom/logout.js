let observer = new MutationObserver(() => {
  const logoutButton = document.querySelector("a.logout");
  if (logoutButton) {
    logoutButton.setAttribute('ng-click', '');
    logoutButton.setAttribute('href', 'logout');
    observer.disconnect();
  }
});

observer.observe(document.body, {
  childList: true,
  subtree: true,
  attributes: false,
  characterData: false
});
