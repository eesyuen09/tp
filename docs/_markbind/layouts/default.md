<head-bottom>
  <link rel="stylesheet" href="{{baseUrl}}/stylesheets/main.css">
</head-bottom>

<header sticky>
  <navbar type="dark">
    <a slot="brand" href="{{baseUrl}}/index.html" title="Home" class="navbar-brand">Tuto</a>
    <li><a href="{{baseUrl}}/index.html" class="nav-link">Home</a></li>
    <li><a href="{{baseUrl}}/UserGuide.html" class="nav-link">User Guide</a></li>
    <li><a href="{{baseUrl}}/DeveloperGuide.html" class="nav-link">Developer Guide</a></li>
    <li><a href="{{baseUrl}}/AboutUs.html" class="nav-link">About Us</a></li>
    <li><a href="https://github.com/se-edu/addressbook-level3" target="_blank" class="nav-link"><md>:fab-github:</md></a>
    </li>
    <li slot="right">
      <form class="navbar-form">
        <searchbar :data="searchData" placeholder="Search" :on-hit="searchCallback" menu-align-right></searchbar>
      </form>
    </li>
  </navbar>
</header>

<div id="flex-body">
  <nav id="site-nav">
    <div class="site-nav-top">
      <div class="fw-bold mb-2" style="font-size: 1.25rem;">Site Map</div>
    </div>
    <div class="nav-component slim-scroll">
      <site-nav>
* [Home]({{ baseUrl }}/index.html)
* [User Guide]({{ baseUrl }}/UserGuide.html) :expanded:
  * [1. Introduction]({{ baseUrl }}/UserGuide.html#1-introduction)
    * [1.1 What is Tuto?]({{ baseUrl }}/UserGuide.html#1-1-what-is-tuto)
    * [1.2 Main Features]({{ baseUrl }}/UserGuide.html#1-2-main-features)
  * [2. Getting Started]({{ baseUrl }}/UserGuide.html#2-getting-started)
    * [2.1 Installation]({{ baseUrl }}/UserGuide.html#2-1-installation)
    * [2.2 How to Use Tuto Commands]({{ baseUrl }}/UserGuide.html#2-2-how-to-use-tuto-commands)
  * [3. Commands]({{ baseUrl }}/UserGuide.html#3-commands)
    * [3.1 Student Management]({{ baseUrl }}/UserGuide.html#3-1-student-management)
      * [3.1.1 Adding a student]({{ baseUrl }}/UserGuide.html#3-1-1-adding-a-student-add)
      * [3.1.2 Editing a student]({{ baseUrl }}/UserGuide.html#3-1-2-editing-a-student-edit)
      * [3.1.3 Finding students]({{ baseUrl }}/UserGuide.html#3-1-3-finding-students-find)
      * [3.1.4 Deleting a student]({{ baseUrl }}/UserGuide.html#3-1-4-deleting-a-student-delete)
      * [3.1.5 Listing all students]({{ baseUrl }}/UserGuide.html#3-1-5-listing-all-students-list)
      * [3.1.6 Clearing all entries]({{ baseUrl }}/UserGuide.html#3-1-6-clearing-all-entries-clear)
    * [3.2 Class Tag Management]({{ baseUrl }}/UserGuide.html#3-2-class-tag-management)
      * [3.2.1 Adding a class tag]({{ baseUrl }}/UserGuide.html#3-2-1-adding-a-class-tag-tag-a)
      * [3.2.2 Deleting a class tag]({{ baseUrl }}/UserGuide.html#3-2-2-deleting-a-class-tag-tag-d)
      * [3.2.3 Listing all class tags]({{ baseUrl }}/UserGuide.html#3-2-3-listing-all-class-tags-tag-l)
    * [3.3 Fee Management]({{ baseUrl }}/UserGuide.html#3-3-fee-management)
      * [3.3.1 Marking as paid]({{ baseUrl }}/UserGuide.html#3-3-1-marking-a-student-as-paid-fee-p)
      * [3.3.2 Marking as unpaid]({{ baseUrl }}/UserGuide.html#3-3-2-marking-a-student-as-unpaid-fee-up)
      * [3.3.3 Viewing payment history]({{ baseUrl }}/UserGuide.html#3-3-4-viewing-a-student-s-payment-history-fee-v)
    * [3.4 Attendance Management]({{ baseUrl }}/UserGuide.html#3-4-attendance-management)
      * [3.4.1 Marking as present]({{ baseUrl }}/UserGuide.html#3-4-1-marking-a-student-as-present-att-p)
      * [3.4.2 Marking as absent]({{ baseUrl }}/UserGuide.html#3-4-2-marking-a-student-as-absent-att-a)
      * [3.4.3 Deleting attendance]({{ baseUrl }}/UserGuide.html#3-4-3-deleting-an-attendance-record-att-d)
      * [3.4.4 Viewing attendance]({{ baseUrl }}/UserGuide.html#3-4-4-viewing-a-student-s-attendance-records-att-v)
    * [3.5 Performance Management]({{ baseUrl }}/UserGuide.html#3-5-performance-management)
      * [3.5.1 Adding a note]({{ baseUrl }}/UserGuide.html#3-5-1-adding-a-performance-note-for-a-student-perf-a)
      * [3.5.2 Editing a note]({{ baseUrl }}/UserGuide.html#3-5-2-editing-a-performance-note-for-a-student-perf-e)
      * [3.5.3 Deleting a note]({{ baseUrl }}/UserGuide.html#3-5-3-deleting-a-performance-note-for-a-student-perf-d)
      * [3.5.4 Viewing notes]({{ baseUrl }}/UserGuide.html#3-5-4-viewing-performance-notes-for-a-student-perf-v)
    * [3.6 Filter Students]({{ baseUrl }}/UserGuide.html#3-6-filter-students-filter)
      * [3.6.1 Filter by paid]({{ baseUrl }}/UserGuide.html#3-6-1-filter-by-paid-status-filter-p)
      * [3.6.2 Filter by unpaid]({{ baseUrl }}/UserGuide.html#3-6-2-filter-by-unpaid-status-filter-up)
      * [3.6.3 Filter by class tag]({{ baseUrl }}/UserGuide.html#3-6-3-filter-by-class-tag-filter-t)
    * [3.7 Viewing help]({{ baseUrl }}/UserGuide.html#3-7-viewing-help-help)
    * [3.8 Exiting the program]({{ baseUrl }}/UserGuide.html#3-8-exiting-the-program-exit)
    * [3.9 Saving the data]({{ baseUrl }}/UserGuide.html#3-9-saving-the-data)
    * [3.10 Editing the data file]({{ baseUrl }}/UserGuide.html#3-10-editing-the-data-file)
  * [FAQ]({{ baseUrl }}/UserGuide.html#faq)
  * [Known issues]({{ baseUrl }}/UserGuide.html#known-issues)
  * [Command Summary]({{ baseUrl }}/UserGuide.html#command-summary)
* [Developer Guide]({{ baseUrl }}/DeveloperGuide.html) :expanded:
  * [Acknowledgements]({{ baseUrl }}/DeveloperGuide.html#acknowledgements)
  * [Setting Up]({{ baseUrl }}/DeveloperGuide.html#setting-up)
  * [Design]({{ baseUrl }}/DeveloperGuide.html#design)
  * [Implementation]({{ baseUrl }}/DeveloperGuide.html#implementation)
  * [Documentation, logging, testing, configuration, dev-ops]({{ baseUrl }}/DeveloperGuide.html#documentation-logging-testing-configuration-dev-ops)
  * [Appendix: Requirements]({{ baseUrl }}/DeveloperGuide.html#appendix-requirements)
  * [Appendix: Instructions for manual testing]({{ baseUrl }}/DeveloperGuide.html#appendix-instructions-for-manual-testing)
* [About Us]({{ baseUrl }}/AboutUs.html)
      </site-nav>
    </div>
  </nav>
  <div id="content-wrapper">
    {{ content }}
  </div>
  <nav id="page-nav">
    <div class="nav-component slim-scroll">
      <page-nav />
    </div>
  </nav>
  <scroll-top-button></scroll-top-button>
</div>

<footer>
  <div class="text-center">
    <small>[<md>**Powered by**</md> <img src="https://markbind.org/favicon.ico" width="30"> {{MarkBind}}, generated on {{timestamp}}]</small>
  </div>
</footer>
