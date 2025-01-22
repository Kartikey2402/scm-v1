console.log("Script loaded");

// change theme start
let currentTheme= getTheme();

//initially
document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
});


function changeTheme(){
    changePageTheme(currentTheme, "");
    //set the listener to change theme button
    const changeThemeButton = document.querySelector('#theme_change_button');
    changeThemeButton.addEventListener("click", (event) => {
        let oldTheme = currentTheme;
        console.log("button clicked");
        if(currentTheme == "dark"){
            currentTheme = "light";
        }
        else{
            currentTheme = "dark";
        }
        changePageTheme(currentTheme, oldTheme);
        
    });

}

// set theme to local storage
function setTheme(theme){
    localStorage.setItem("theme", theme);
}
// get theme from local storage 
function getTheme(){
    let theme=localStorage.getItem("theme");
    if(theme){
        return theme;
    }
    else{
        return "light";
    }
}

// change current page theme 
function changePageTheme(theme, oldTheme){
    // update in local storage
    setTheme(currentTheme);

    //remove the current theme
    if(oldTheme){
        document.querySelector('html').classList.remove(oldTheme);
    }

    // set the current theme
    document.querySelector('html').classList.add(theme);
    // change the text of button
    document.querySelector('#theme_change_button').querySelector("span").textContent=
    theme =='light' ? 'Dark':'Light';
}

// change page theme end