# Simple Android Common Cocktail
- InApp Update
- InApp review
- Startup CustomTab

# Add to your project
>Step 1. Add the JitPack repository to your build file
```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
>Step 2. Add the dependency
```gradle
dependencies {
	        implementation 'com.github.swacodelabs:AndroidCommonCocktail:1.0.0'
	}
```
# Add to your project

* Add this line to Application Class
  
  ```code
  ApiConfigration(context,BuildConfig.APPLICATION_ID,BuildConfig.VERSION_NAME,BuildConfig.SECREATE_KEY)
  ```

* Add this line to Launcher activity for InAppUpdate Functionality
  
  ```code
   InAppUpdateConfrigration(this@MainActivity) 
  ```
* Add this where you want to add a review Dailog
  
  ```code
     var inappReview = RatingDailog(this@MainActivity)
     inappReview.show()

  ```






  
