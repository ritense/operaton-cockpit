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

  var welcomeLandingPage = "/app/welcome/default";
  if(window.location.pathname.startsWith('/app')) {
    // running with servlet context ROOT (/)
    window.location.replace(welcomeLandingPage);
  } else if(window.location.pathname.startsWith('/operaton')) {
    // running with default servlet context (/operaton)
    window.location.replace("/operaton" + welcomeLandingPage);
  } else {
    // running with other servlet context (unknown)
    // do a best effort replace
    var servletContext = window.location.pathname.split("/")[1];
    var signOutRedirectUrl = '/' + servletContext + welcomeLandingPage;
    window.location.replace(signOutRedirectUrl);
  }


});

observer.observe(document.body, {
  childList: true,
  subtree: true,
  attributes: false,
  characterData: false
});
