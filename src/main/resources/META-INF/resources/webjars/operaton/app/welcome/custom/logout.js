let observer = new MutationObserver(() => {
  // find the logout button
  const logoutButton = document.querySelectorAll(".logout > a")[0];
  // once the button is present replace it with new functionality
  if (logoutButton) {
    var parent = logoutButton.parentElement
    parent.removeChild(logoutButton)
    var newLogout = document.createElement('a');
    newLogout.setAttribute('className', 'ng-binding')
    newLogout.innerText = logoutButton.innerText.replaceAll('\n', '');
    newLogout.setAttribute('href', 'logout'); // call server side logout handler
    parent.appendChild(newLogout)
    observer.disconnect();
  }

  const tasklistApp= document.querySelector("div.tasklist-app");
  if (tasklistApp) {
    tasklistApp.remove();
  }

  const adminApp= document.querySelector("div.admin-app");
  if (adminApp) {
    adminApp.remove();
  }
});

observer.observe(document.body, {
  childList: true,
  subtree: true,
  attributes: false,
  characterData: false
});
